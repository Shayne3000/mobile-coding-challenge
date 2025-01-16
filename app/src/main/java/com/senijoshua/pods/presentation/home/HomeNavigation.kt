package com.senijoshua.pods.presentation.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
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
        // TODO Add home screen composable
    }
}
