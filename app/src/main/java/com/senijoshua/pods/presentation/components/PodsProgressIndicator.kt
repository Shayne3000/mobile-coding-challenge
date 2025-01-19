package com.senijoshua.pods.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.senijoshua.pods.R
import com.senijoshua.pods.presentation.theme.PodsTheme

const val PROGRESS_TAG: String = "PodsProgress"

/**
 * Reusable wrapper over [CircularProgressIndicator] with which an
 * indeterminate progress bar of size, [size] can be setup
 * in a given layout.
 */
@Composable
fun PodsProgressIndicator(modifier: Modifier = Modifier, size: Dp) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .testTag(PROGRESS_TAG)
                .size(size),
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
@Preview(showSystemUi = true)
private fun PodsProgressIndicatorPreview() {
    PodsTheme {
        PodsProgressIndicator(size = dimensionResource(id = R.dimen.progress_size_large))
    }
}
