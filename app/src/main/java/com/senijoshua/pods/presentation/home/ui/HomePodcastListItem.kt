package com.senijoshua.pods.presentation.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import coil.compose.AsyncImage
import com.senijoshua.pods.R
import com.senijoshua.pods.presentation.home.model.HomePodcast
import com.senijoshua.pods.presentation.home.model.fakePodcastList
import com.senijoshua.pods.presentation.theme.PodsTheme
import com.senijoshua.pods.presentation.util.buildAsyncImage

/**
 * List item of the [HomePodcastList].
 */
@Composable
fun HomePodcastListItem(
    modifier: Modifier = Modifier,
    podcast: HomePodcast,
    onClick: (String) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .clickable(onClick = {
                onClick(podcast.id)
            }),
    ) {
        Row(
            modifier = Modifier
                .padding(
                    top = dimensionResource(R.dimen.padding_medium),
                    start = dimensionResource(R.dimen.padding_medium))
        ) {
            val imageRequest = buildAsyncImage(podcast.thumbnail)

            AsyncImage(
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.avatar_size_medium))
                    .clip(RoundedCornerShape(dimensionResource(id = R.dimen.radius_small))),
                model = imageRequest,
                contentDescription = stringResource(id = R.string.podcast_item_thumbnail_content_desc),
                contentScale = ContentScale.Crop,
            )

            Column(
                modifier = Modifier.padding(
                    start = dimensionResource(R.dimen.padding_medium)
                ),
            ) {
                Text(
                    modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_small)),
                    text = podcast.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )

                Text(
                    modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_small)),
                    text = podcast.publisher,
                    style = MaterialTheme.typography.bodyMedium,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )

                if (podcast.isFavourite) {
                    Text(
                        modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_small)),
                        text = stringResource(R.string.favourited_podcast_item),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }

        HorizontalDivider(
            modifier = Modifier
                .padding(
                    top = dimensionResource(R.dimen.padding_medium),
                    start = dimensionResource(R.dimen.padding_medium)
                ),
            thickness = dimensionResource(R.dimen.divider_height),
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@PreviewLightDark
@Composable
private fun HomePodcastListItemPreview() {
    PodsTheme {
        HomePodcastListItem(podcast = fakePodcastList[0])
    }
}
