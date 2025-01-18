package com.senijoshua.pods.data.repository

import com.senijoshua.pods.presentation.detail.model.DetailPodcast
import com.senijoshua.pods.presentation.home.model.HomePodcast
import kotlinx.coroutines.flow.Flow

/**
 * This is the abstraction through which the presentation layer
 * communicates with the data layer.
 */
interface PodcastRepository {
    suspend fun getPodcasts(page: Int): Flow<Result<List<HomePodcast>>>
    suspend fun getPodcastGivenId(podcastId: String): Result<DetailPodcast>
}
