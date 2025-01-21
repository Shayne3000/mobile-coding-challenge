package com.senijoshua.pods.presentation.home.model

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * Representation of an Podcast type with data specific to the home screen.
 */
data class HomePodcast(
    val id: String,
    val title: String,
    val thumbnail: String,
    val publisher: String,
    val isFavourite: Boolean,
)

/**
 * Dummy list of [HomePodcast] for preview and testing purposes.
 */
val fakePodcastList = List(10) { index ->
    HomePodcast(
        index.toString(),
        "Podcast $index",
        "Podcast $index image",
        "PUBLISHER $index",
        index % 2 == 0,
    )
}

/**
 * "Paged" dummy podcast data for preview and testing purposes.
 */
val fakePagedPodcastList: Flow<PagingData<HomePodcast>> =
    flowOf(PagingData.from(fakePodcastList))
