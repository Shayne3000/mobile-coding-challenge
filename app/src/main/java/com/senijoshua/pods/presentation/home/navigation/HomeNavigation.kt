package com.senijoshua.pods.presentation.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.senijoshua.pods.presentation.home.ui.HomeScreen
import kotlinx.serialization.Serializable

/**
 * Route for the Home screen composable
 */
@Serializable
object HomeRoute

/**
 * NavGraphBuilder extension function that adds the HomeScreen composable as a destination
 * in the app-level nav graph.
 */
fun NavGraphBuilder.homeScreen(
    toDetailScreen: (String) -> Unit,
) {
    composable<HomeRoute> {
        HomeScreen(onNavigateToDetail = { podcastId ->
            toDetailScreen(podcastId)
        })
    }
}
