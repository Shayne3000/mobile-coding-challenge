@file:OptIn(ExperimentalMaterial3Api::class)

package com.senijoshua.pods.presentation.detail.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.senijoshua.pods.R
import com.senijoshua.pods.presentation.components.PodsEmptyScreen
import com.senijoshua.pods.presentation.components.PodsProgressIndicator
import com.senijoshua.pods.presentation.detail.model.DetailPodcast
import com.senijoshua.pods.presentation.detail.model.fakeDetailPodcastArticle
import com.senijoshua.pods.presentation.detail.stateholder.DetailUiState
import com.senijoshua.pods.presentation.detail.stateholder.DetailViewModel
import com.senijoshua.pods.presentation.theme.PodsTheme
import com.senijoshua.pods.presentation.util.buildAsyncImage

@Composable
fun DetailScreen(
    vm: DetailViewModel = hiltViewModel(),
    onBackClicked: () -> Unit = {},
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    DetailContent(
        uiState = uiState,
        favouriteStatusChanged = { podcastId ->
            vm.toggleFavouriteStatus(podcastId)
        },
        errorShown = {
            vm.resetErrorState()
        },
        backClicked = {
            onBackClicked()
        }
    )

    LaunchedEffect(Unit) {
        vm.getPodcast()
    }
}

@Composable
fun DetailContent(
    modifier: Modifier = Modifier,
    uiState: DetailUiState,
    favouriteStatusChanged: (String) -> Unit = {},
    errorShown: () -> Unit = {},
    backClicked: () -> Unit = {},
) {
    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold(topBar = {
        TopAppBar(
            title = {},
            navigationIcon = {
                IconButton(onClick = { backClicked() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.back_content_desc),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
            )
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
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
                .padding(padding)
        ) {
            if (uiState.isLoading) {
                PodsProgressIndicator(
                    modifier = modifier,
                    size = dimensionResource(R.dimen.progress_size_large)
                )
            } else if (uiState.details != null) {
                PodcastDetail(
                    podcastDetail = uiState.details,
                    changeFavouriteStatus = { podcastId ->
                        favouriteStatusChanged(podcastId)
                    })
            } else {
                PodsEmptyScreen(
                    modifier,
                    text = R.string.no_podcast_detail_text,
                    iconContentDescription = R.string.no_podcast_content_desc
                )
            }
        }
    }

    uiState.errorMessage?.let { message ->
        LaunchedEffect(message) {
            snackBarHostState.showSnackbar(message)
            errorShown()
        }
    }
}

@Composable
fun PodcastDetail(
    modifier: Modifier = Modifier,
    podcastDetail: DetailPodcast,
    changeFavouriteStatus: (String) -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(
                start = dimensionResource(R.dimen.padding_medium),
                end = dimensionResource(R.dimen.padding_medium)
            )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier
                    .padding(
                        top = dimensionResource(R.dimen.padding_small)
                    ),
                text = podcastDetail.title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimary,
            )

            Text(
                modifier = Modifier
                    .padding(
                        top = dimensionResource(R.dimen.padding_small),
                    ),
                text = podcastDetail.publisher,
                style = MaterialTheme.typography.bodyMedium,
                fontStyle = FontStyle.Italic,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )

            AsyncImage(
                model = buildAsyncImage(imageUrl = podcastDetail.image),
                contentDescription = stringResource(id = R.string.detail_podcast_img_content_desc),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(dimensionResource(R.dimen.avatar_size_large))
                    .padding(top = dimensionResource(R.dimen.padding_large))
                    .clip(RoundedCornerShape(dimensionResource(id = R.dimen.radius_small)))
            )

            val buttonText = if (podcastDetail.isFavourite) {
                stringResource(R.string.favourited_btn_text)
            } else {
                stringResource(
                    R.string.favourite_btn_text
                )
            }

            Button(
                modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_medium)),
                onClick = {
                    changeFavouriteStatus(podcastDetail.id)
                },
                shape = RoundedCornerShape(dimensionResource(R.dimen.padding_small)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                )
            ) {
                Text(
                    text = buttonText,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = dimensionResource(R.dimen.padding_large),
                    start = dimensionResource(R.dimen.padding_medium),
                    end = dimensionResource(R.dimen.padding_medium)
                ),
            text = podcastDetail.description,
            color = MaterialTheme.colorScheme.onTertiaryContainer,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
        )
    }
}

@PreviewLightDark
@Composable
private fun DetailPreview() {
    PodsTheme {
        DetailContent(
            uiState = DetailUiState(
                details = fakeDetailPodcastArticle,
                isLoading = false
            )
        )
    }
}
