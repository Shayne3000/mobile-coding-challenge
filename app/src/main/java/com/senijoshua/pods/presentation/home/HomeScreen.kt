package com.senijoshua.pods.presentation.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.senijoshua.pods.presentation.theme.PodsTheme

@Composable
fun HomeScreen(
    vm: HomeViewModel = hiltViewModel(),
    onNavigateToDetail: (String) -> Unit,
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    HomeContent(
        uiState = uiState,
        navigateToDetail = { podcastId ->
            onNavigateToDetail(podcastId)
        }
    )
}

@Composable
fun HomeContent(
    uiState: HomeUiState,
    navigateToDetail: (String) -> Unit = {},
) {

}

@PreviewLightDark
@Composable
fun HomePreview() {
    PodsTheme {
        HomeContent(uiState = HomeUiState())
    }
}
