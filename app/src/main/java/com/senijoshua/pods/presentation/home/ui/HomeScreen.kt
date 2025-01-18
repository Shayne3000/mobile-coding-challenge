@file:OptIn(ExperimentalMaterial3Api::class)

package com.senijoshua.pods.presentation.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.senijoshua.pods.R
import com.senijoshua.pods.presentation.components.PodsEmptyScreen
import com.senijoshua.pods.presentation.components.PodsProgressIndicator
import com.senijoshua.pods.presentation.home.stateholder.HomeUiState
import com.senijoshua.pods.presentation.home.stateholder.HomeViewModel
import com.senijoshua.pods.presentation.home.model.HomePodcast
import com.senijoshua.pods.presentation.home.model.fakePodcastList
import com.senijoshua.pods.presentation.theme.PodsTheme

@Composable
fun HomeScreen(
    vm: HomeViewModel = hiltViewModel(),
    onNavigateToDetail: (String) -> Unit,
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    HomeContent(
        uiState = uiState,
        loadNextPage = {
            vm.getPagedPodcasts()
        },
        refresh = {
            vm.refreshPagedPodcasts()
        },
        resetErrorState = {
            vm.onResetErrorState()
        },
        navigateToDetail = { podcastId ->
            onNavigateToDetail(podcastId)
        }
    )

    LaunchedEffect(Unit) {
        vm.getPagedPodcasts()
    }
}

@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    uiState: HomeUiState,
    loadNextPage: () -> Unit = {},
    refresh: () -> Unit = {},
    resetErrorState: () -> Unit = {},
    navigateToDetail: (String) -> Unit = {},
) {
    val snackBarHostState = remember { SnackbarHostState() }

    val pullToRefreshState = rememberPullToRefreshState()

    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(id = R.string.home_title),
                    style = MaterialTheme.typography.titleLarge
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
            ),
        )
    }, snackbarHost = {
        SnackbarHost(hostState = snackBarHostState) { data ->
            Snackbar(
                snackbarData = data,
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError
            )
        }
    }) { padding ->
        PullToRefreshBox(
            modifier = modifier
                .fillMaxSize()
                .padding(padding),
            isRefreshing = uiState.isRefreshing,
            state = pullToRefreshState,
            onRefresh = {
                refresh()
            },
            indicator = {
                Indicator(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = dimensionResource(R.dimen.padding_xlarge)),
                    isRefreshing = uiState.isRefreshing,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    state = pullToRefreshState
                )
            }
        ) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background)
            ) {
                if (uiState.isLoading) {
                    PodsProgressIndicator(
                        modifier = modifier,
                        size = dimensionResource(R.dimen.progress_size_large)
                    )
                } else if (uiState.isRefreshing) {
                    RefreshText(modifier)
                } else if (uiState.podcasts.isNotEmpty()) {
                    HomePodcastList(
                        podcasts = uiState.podcasts,
                        isPaging = uiState.isPaging,
                        hasPagedData = uiState.hasPagedData,
                        loadNextPage = {
                            loadNextPage()
                        },
                        onPodcastItemClicked = { podcastId ->
                            navigateToDetail(podcastId)
                        })
                } else {
                    PodsEmptyScreen(
                        modifier,
                        text = R.string.empty_podcasts_text,
                        iconContentDescription = R.string.empty_podcasts_content_desc
                    )
                }
            }
        }
    }

    uiState.errorMessage?.let { message ->
        LaunchedEffect(message, snackBarHostState) {
            snackBarHostState.showSnackbar(message)
            resetErrorState()
        }
    }
}

@Composable
fun RefreshText(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.refresh_text),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun HomePodcastList(
    modifier: Modifier = Modifier,
    podcasts: List<HomePodcast>,
    isPaging: Boolean,
    hasPagedData: Boolean,
    loadNextPage: () -> Unit = {},
    onPodcastItemClicked: (String) -> Unit = {},
) {
    val listState = rememberLazyListState()

    val hasScrolledNearEnd by remember {
        derivedStateOf {
            val lastVisibleItemIndex =
                listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
            val totalItemsCount = listState.layoutInfo.totalItemsCount
            lastVisibleItemIndex >= totalItemsCount - 2 && totalItemsCount > 0
        }
    }

    if (hasScrolledNearEnd && !isPaging && hasPagedData) {
        loadNextPage()
    }

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = dimensionResource(id = R.dimen.padding_small)),
    ) {
        items(items = podcasts, key = { podcast -> podcast.id }) { podcast ->
            HomePodcastListItem(podcast = podcast, onClick = { podcastId ->
                onPodcastItemClicked(podcastId)
            })
        }

        if (isPaging) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(R.dimen.padding_medium)),
                    contentAlignment = Alignment.Center
                ) {
                    PodsProgressIndicator(
                        size = dimensionResource(R.dimen.progress_size_medium)
                    )
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun HomePreview() {
    PodsTheme {
        HomeContent(uiState = HomeUiState(podcasts = fakePodcastList))
    }
}
