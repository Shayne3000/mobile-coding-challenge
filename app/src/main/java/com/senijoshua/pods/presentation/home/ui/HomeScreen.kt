@file:OptIn(ExperimentalMaterial3Api::class)

package com.senijoshua.pods.presentation.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.senijoshua.pods.R
import com.senijoshua.pods.presentation.components.PodsEmptyScreen
import com.senijoshua.pods.presentation.components.PodsProgressIndicator
import com.senijoshua.pods.presentation.home.model.HomePodcast
import com.senijoshua.pods.presentation.home.model.fakePagedPodcastList
import com.senijoshua.pods.presentation.home.stateholder.HomeViewModel
import com.senijoshua.pods.presentation.theme.PodsTheme

@Composable
fun HomeScreen(
    vm: HomeViewModel = hiltViewModel(),
    onNavigateToDetail: (String) -> Unit = {},
) {
    val podcasts = vm.pagedPodcasts.collectAsLazyPagingItems()

    HomeContent(
        pagedPodcasts = podcasts,
        navigateToDetail = { podcastId ->
            onNavigateToDetail(podcastId)
        }
    )
}

@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    pagedPodcasts: LazyPagingItems<HomePodcast>,
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
            isRefreshing = pagedPodcasts.loadState.refresh is LoadState.Loading && pagedPodcasts.itemCount != 0,
            state = pullToRefreshState,
            onRefresh = {
                pagedPodcasts.refresh()
            },
            indicator = {
                Indicator(
                    modifier = Modifier
                        .align(Alignment.TopCenter),
                    isRefreshing = pagedPodcasts.loadState.refresh is LoadState.Loading && pagedPodcasts.itemCount != 0,
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
                if (pagedPodcasts.loadState.refresh is LoadState.Loading && pagedPodcasts.itemCount == 0) {
                    PodsProgressIndicator(
                        modifier = modifier,
                        size = dimensionResource(R.dimen.progress_size_large)
                    )
                } else if ((pagedPodcasts.loadState.refresh is LoadState.Error) ||
                    ((pagedPodcasts.loadState.refresh is LoadState.NotLoading)) &&
                    pagedPodcasts.itemCount == 0
                ) {
                    PodsEmptyScreen(
                        Modifier.padding(horizontal = dimensionResource(R.dimen.padding_medium)),
                        text = R.string.empty_podcasts_text,
                        iconContentDescription = R.string.empty_podcasts_content_desc
                    )
                } else {
                    HomePodcastList(
                        podcasts = pagedPodcasts,
                        onPodcastClicked = { podcastId ->
                            navigateToDetail(podcastId)
                        }
                    )
                }
            }
        }
    }

    if (pagedPodcasts.loadState.refresh is LoadState.Error) {
        val refreshError = pagedPodcasts.loadState.refresh as LoadState.Error
        val errorMessage = refreshError.error.message

        errorMessage?.let { message ->
            LaunchedEffect(message) {
                snackBarHostState.showSnackbar(message)
            }
        }
    }
}

@Composable
fun HomePodcastList(
    modifier: Modifier = Modifier,
    podcasts: LazyPagingItems<HomePodcast>,
    onPodcastClicked: (String) -> Unit = {},
) {
    LazyColumn(
        modifier = modifier.testTag(LIST_TAG),
    ) {
        items(podcasts.itemCount) { index ->
            val homePodcast = podcasts[index]
            homePodcast?.let { podcast ->
                HomePodcastListItem(podcast = podcast, onClick = { podcastId ->
                    onPodcastClicked(podcastId)
                })
            }
        }

        when (podcasts.loadState.append) {
            is LoadState.Loading -> {
                item {
                    PodsProgressIndicator(
                        modifier = modifier,
                        size = dimensionResource(R.dimen.progress_size_medium)
                    )
                }
            }
            is LoadState.Error -> {
                item {
                    AppendErrorItem(
                        podcasts = podcasts
                    )
                }
            }
            else -> {
                // No Op
            }
        }
    }
}

@Composable
private fun AppendErrorItem(
    modifier: Modifier = Modifier,
    podcasts: LazyPagingItems<HomePodcast>,
) {
    val appendError = podcasts.loadState.append as LoadState.Error

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .padding(
                dimensionResource(R.dimen.padding_small)
            )
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(R.dimen.padding_small)),
            text = appendError.error.localizedMessage!!,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2
        )
        Button(
            shape = RoundedCornerShape(dimensionResource(R.dimen.padding_small)),
            onClick = {
                podcasts.retry()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
        ) {
            Text(
                text = stringResource(R.string.retry_podcast_list_append),
            )
        }
    }
}

const val LIST_TAG = "HomePodcastList"

@PreviewLightDark
@Composable
private fun HomePreview() {
    PodsTheme {
        HomeContent(pagedPodcasts = fakePagedPodcastList.collectAsLazyPagingItems())
    }
}
