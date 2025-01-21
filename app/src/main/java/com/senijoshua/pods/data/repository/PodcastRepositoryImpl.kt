@file:OptIn(ExperimentalPagingApi::class)

package com.senijoshua.pods.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.RemoteMediator
import androidx.paging.map
import com.senijoshua.pods.data.local.podcast.PodcastDao
import com.senijoshua.pods.data.local.podcast.PodcastEntity
import com.senijoshua.pods.data.util.asResult
import com.senijoshua.pods.data.util.toDetailPodcast
import com.senijoshua.pods.data.util.toHomePodcast
import com.senijoshua.pods.presentation.detail.model.DetailPodcast
import com.senijoshua.pods.presentation.home.model.HomePodcast
import com.senijoshua.pods.util.GlobalConstants
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * [PodcastRepository] implementation that abstracts away details of data retrieval
 * from the presentation layer and retrieves paged podcast data in an offline-first
 * manner.
 */
class PodcastRepositoryImpl @Inject constructor(
    private val local: PodcastDao,
    private val remoteMediator: RemoteMediator<Int, PodcastEntity>,
    private val dispatcher: CoroutineDispatcher,
) : PodcastRepository {

    /**
     * Get the paged list of podcasts as an observable stream.
     */
    override suspend fun getPagedPodcasts(): Flow<PagingData<HomePodcast>> {
        return withContext(dispatcher) {
            Pager(
                config = PagingConfig(
                    pageSize = GlobalConstants.MAX_PODCASTS_PER_PAGE,
                    prefetchDistance = 0,
                    initialLoadSize = GlobalConstants.MAX_PODCASTS_PER_PAGE * 2,
                ),
                remoteMediator = remoteMediator,
                pagingSourceFactory = { local.getPagedPodcasts() }
            ).flow.map { pagingData: PagingData<PodcastEntity> ->
                pagingData.map { podcastEntity ->
                    podcastEntity.toHomePodcast()
                }
            }
        }
    }

    override suspend fun getPodcastGivenId(podcastId: String): Flow<Result<DetailPodcast>> {
        return local.getPodcastGivenId(podcastId).map { podcast ->
            podcast.toDetailPodcast()
        }.flowOn(dispatcher).asResult()
    }

    override suspend fun togglePodcastFavouriteStatus(podcastId: String) {
        withContext(dispatcher) {
            local.favouritePodcast(podcastId)
        }
    }
}
