package com.senijoshua.pods.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Response model for the getBestPodcasts endpoint.
 */
@Serializable
data class PodcastsResponse(
    @SerialName("previous_page_number")
    val previousPageNumber: Int,
    @SerialName("page_number")
    val pageNumber: Int,
    @SerialName("next_page_number")
    val nextPageNumber: Int,
    val podcasts: List<RemotePodcast>
)

/**
 * Network model for a Podcast
 */
@Serializable
data class RemotePodcast(
    val id: String,
    val title: String,
    val thumbnail: String,
    val image: String,
    val publisher: String,
    val description: String,
)
