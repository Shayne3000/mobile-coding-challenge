@file:OptIn(ExperimentalPagingApi::class)

package com.senijoshua.pods.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.senijoshua.pods.data.local.FakePodcastDao
import com.senijoshua.pods.data.local.podcast.PodcastEntity
import com.senijoshua.pods.data.remote.FakePodcastApi
import com.senijoshua.pods.data.util.toLocal

/**
 * Fake implementation of [RemoteMediator] for testing purposes
 */
class FakePodcastRemoteMediator(
    private val localDataSource: FakePodcastDao,
    private val remoteDataSource: FakePodcastApi
) : RemoteMediator<Int, PodcastEntity>(){

    var shouldThrowError = false

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PodcastEntity>
    ): MediatorResult {
        return if (shouldThrowError) {
            MediatorResult.Error(Throwable())
        } else {
            val response = remoteDataSource.getBestPodcasts(1)
            val remotePodcasts = response.podcasts
            val endOfPaginationReached = remotePodcasts.isEmpty()

            if (loadType == LoadType.REFRESH) {
                localDataSource.clearPodcasts()
            }

            localDataSource.insertPodcasts(remotePodcasts.toLocal())

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        }
    }
}
