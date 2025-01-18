package com.senijoshua.pods.presentation.home.model

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
 * Dummy list of HomePodcasts for preview and testing purposes.
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
