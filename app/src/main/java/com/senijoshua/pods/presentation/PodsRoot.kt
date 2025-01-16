package com.senijoshua.pods.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.senijoshua.pods.presentation.detail.DetailRoute
import com.senijoshua.pods.presentation.detail.detailScreen
import com.senijoshua.pods.presentation.home.HomeRoute
import com.senijoshua.pods.presentation.home.homeScreen

/**
 * App-level composable for App-level UI components and logic.
 */
@Composable
fun PodsRoot(
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = HomeRoute) {
        homeScreen {
            navController.navigate(route = DetailRoute)
        }
        detailScreen {
            navController.popBackStack()
        }
    }
}
