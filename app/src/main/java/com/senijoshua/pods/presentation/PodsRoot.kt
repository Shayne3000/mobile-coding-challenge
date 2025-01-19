package com.senijoshua.pods.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.senijoshua.pods.presentation.detail.navigation.DetailRoute
import com.senijoshua.pods.presentation.detail.navigation.detailScreen
import com.senijoshua.pods.presentation.home.navigation.HomeRoute
import com.senijoshua.pods.presentation.home.navigation.homeScreen

/**
 * App-level composable for App-level UI components and logic.
 */
@Composable
fun PodsRoot(
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = HomeRoute) {
        homeScreen { podcastId ->
            navController.navigate(route = DetailRoute(podcastId))
        }
        detailScreen {
            navController.popBackStack()
        }
    }
}
