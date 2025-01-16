package com.senijoshua.pods.presentation.detail

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

/**
 * Route for the Detail screen composable that takes a podcast id as an argument
 */
@Serializable
data class DetailRoute(val podcastId: String)

/**
 * NavGraphBuilder extension function that adds the DetailScreen composable as a destination
 * in the app-level nav graph.
 */
fun NavGraphBuilder.detailScreen(onBackClicked: () -> Unit) {
    composable<DetailRoute> {
       // TODO Add Detail Screen composable
    }
}
