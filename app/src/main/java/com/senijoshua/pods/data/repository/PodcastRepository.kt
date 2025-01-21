package com.senijoshua.pods.data.repository

import androidx.paging.PagingData
import com.senijoshua.pods.presentation.detail.model.DetailPodcast
import com.senijoshua.pods.presentation.home.model.HomePodcast
import kotlinx.coroutines.flow.Flow

/**
 * Abstraction through which the presentation layer
 * communicates with the data layer.
 */
interface PodcastRepository {
    suspend fun getPagedPodcasts(): Flow<PagingData<HomePodcast>>
    suspend fun getPodcastGivenId(podcastId: String): Flow<Result<DetailPodcast>>
    suspend fun togglePodcastFavouriteStatus(podcastId: String)
}
