package com.senijoshua.pods.data.util

import com.senijoshua.pods.data.local.podcast.PodcastEntity
import com.senijoshua.pods.data.remote.RemotePodcast
import com.senijoshua.pods.presentation.detail.model.DetailPodcast
import com.senijoshua.pods.presentation.home.model.HomePodcast

/**
 * Extension functions for converting data models to types relevant for the architectural
 * layer in which they are used.
 */

/**
 * Converts the network model to the local representation for persistence in the DB.
 */
fun RemotePodcast.toLocal() = PodcastEntity(
    podcastId = id,
    title = title,
    thumbnail = thumbnail,
    image = image,
    publisher = publisher,
    description = description,
    isFavourite = false,
)

fun List<RemotePodcast>.toLocal() = map(RemotePodcast::toLocal)

/**
 * Converts the local model to the model relevant for the home screen in the presentation layer.
 */
fun PodcastEntity.toHomePodcast() = HomePodcast(
    id = id.toString(),
    title = title,
    thumbnail = thumbnail,
    publisher = publisher,
    isFavourite = isFavourite
)

fun List<PodcastEntity>.toHomePodcast() = map(PodcastEntity::toHomePodcast)

/**
 * Converts the local model to the model relevant for the detail screen in the presentation layer.
 */
fun PodcastEntity.toDetailPodcast() = DetailPodcast(
    id = id.toString(),
    title = title,
    image = image,
    publisher = publisher,
    description = description,
    isFavourite = isFavourite
)
