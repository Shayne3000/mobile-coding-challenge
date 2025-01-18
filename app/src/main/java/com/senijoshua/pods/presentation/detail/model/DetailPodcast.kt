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
