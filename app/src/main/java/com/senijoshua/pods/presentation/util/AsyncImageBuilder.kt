package com.senijoshua.pods.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.senijoshua.pods.R

/**
 * Encapsulation for the specifics required for loading images into
 * a Coil AsyncImage composable.
 */
@Composable
fun buildAsyncImage(imageUrl: String) = ImageRequest.Builder(LocalContext.current)
    .data(imageUrl)
    .placeholder(R.drawable.ic_podcast_placeholder)
    .error(R.drawable.ic_podcast_placeholder)
    .fallback(R.drawable.ic_podcast_placeholder)
    .memoryCacheKey(imageUrl)
    .memoryCachePolicy(CachePolicy.ENABLED)
    .diskCacheKey(imageUrl)
    .diskCachePolicy(CachePolicy.ENABLED)
    .build()
