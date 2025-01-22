package com.senijoshua.pods.presentation.detail.model

/**
 * Representation of an Podcast type with data specific to the detail screen.
 */
data class DetailPodcast(
    val id: String,
    val title: String,
    val image: String,
    val publisher: String,
    val description: String,
    val isFavourite: Boolean,
)

val fakeDetailPodcastArticle = DetailPodcast(
    id = "Podcast Id",
    title = "Podcast title",
    image = "Podcast Image",
    publisher = "Podcast publisher",
    description = "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut " +
            "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate" +
            " velit esse cillum dolore eu fugiat nulla pariatur.",
    isFavourite = false,
)
