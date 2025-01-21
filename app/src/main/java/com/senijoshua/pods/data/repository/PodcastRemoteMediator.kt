@file:OptIn(ExperimentalPagingApi::class)

package com.senijoshua.pods.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.senijoshua.pods.data.local.podcast.PodcastDao
import com.senijoshua.pods.data.local.podcast.PodcastEntity
import com.senijoshua.pods.data.local.remotekey.RemoteKeyDao
import com.senijoshua.pods.data.local.remotekey.RemoteKeyEntity
import com.senijoshua.pods.data.local.util.dbcache.DbCacheLimit
import com.senijoshua.pods.data.local.util.dbtransaction.PodsDbTransactionProvider
import com.senijoshua.pods.data.remote.PodcastApi
import com.senijoshua.pods.data.util.toLocal
import com.senijoshua.pods.util.GlobalConstants
import javax.inject.Inject

/**
 * RemoteMediator implementation for retrieving paged data
 * from remote and local data sources.
 */
class PodcastRemoteMediator @Inject constructor(
    private val localPodcastSource: PodcastDao,
    private val localRemoteKeySource: RemoteKeyDao,
    private val remotePodcastSource: PodcastApi,
    private val dbTransactionProvider: PodsDbTransactionProvider,
    private val cacheLimit: DbCacheLimit,
) : RemoteMediator<Int, PodcastEntity>() {

    /**
     * Determinant for whether the locally cached data should be
     * invalidated and a remote refresh should be triggered.
     */
    override suspend fun initialize(): InitializeAction {
        return if (System.currentTimeMillis() - (localRemoteKeySource.getCreatedTime()
                ?: 0) <= cacheLimit.clearCacheLimit
        ) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PodcastEntity>
    ): MediatorResult {
        val pageToLoad: Int = when (loadType) {
            LoadType.REFRESH -> {
                GlobalConstants.INITIAL_PAGE
            }
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val remoteKeyForLastPodcastItem = state.lastItemOrNull()?.let {
                    // Due to the backend limitations, the page number doesn't matter
                    // as much as the mechanics of appropriately loading more data
                    // after the last loaded item.
                    localRemoteKeySource.getLastInsertedRemoteKey()
                }

                remoteKeyForLastPodcastItem?.nextPageKey
                    ?: return MediatorResult.Success(
                        endOfPaginationReached = remoteKeyForLastPodcastItem != null
                    )
            }
        }

        try {
            val response = remotePodcastSource.getBestPodcasts(pageToLoad)
            val remotePodcasts = response.podcasts
            val loadedPage = response.pageNumber
            val endOfPaginationReached = remotePodcasts.isEmpty()


            dbTransactionProvider.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    localPodcastSource.clearPodcasts()
                    localRemoteKeySource.clearRemoteKeys()
                }

                val prevPageKey = if (loadedPage > 1) loadedPage - 1 else null
                val nextPageKey = if (endOfPaginationReached) null else loadedPage + 1

                val remoteKeys = remotePodcasts.map { podcast ->
                    RemoteKeyEntity(
                        podcastId = podcast.id,
                        prevPageKey = prevPageKey,
                        currentPageKey = loadedPage,
                        nextPageKey = nextPageKey
                    )
                }

                localPodcastSource.insertPodcasts(remotePodcasts.toLocal())
                localRemoteKeySource.insertRemoteKey(remoteKeys)
            }

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: Exception) {
            if (state.isEmpty()) {
                return MediatorResult.Error(exception)
            }

            return MediatorResult.Success(endOfPaginationReached = true)
        }
    }
}
