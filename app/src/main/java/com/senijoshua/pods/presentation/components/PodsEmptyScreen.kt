package com.senijoshua.pods.presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.senijoshua.pods.R
import com.senijoshua.pods.presentation.theme.PodsTheme

/**
 * Component for when there is/are no podcast(s)
 * on successful return for use in screens.
 */
@Composable
fun PodsEmptyScreen(
    modifier: Modifier = Modifier,
    @StringRes text: Int,
    @StringRes iconContentDescription: Int,
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_empty),
            contentDescription = stringResource(
                id = iconContentDescription
            ),
            tint = MaterialTheme.colorScheme.onBackground
        )
        Text(
            modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_xsmall)),
            text = stringResource(id = text),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
@Preview(showSystemUi = true)
private fun PulitzerProgressIndicatorPreview() {
    PodsTheme {
        PodsEmptyScreen(
            text = R.string.empty_podcasts_text,
            iconContentDescription =  R.string.empty_podcasts_content_desc
        )
    }
}
