package com.senijoshua.pods.presentation.detail.ui

import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.hasTextExactly
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.senijoshua.pods.R
import com.senijoshua.pods.presentation.components.PROGRESS_TAG
import com.senijoshua.pods.presentation.detail.model.DetailPodcast
import com.senijoshua.pods.presentation.detail.model.fakeDetailPodcastArticle
import com.senijoshua.pods.presentation.theme.PodsTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Instrumented unit test for [PodcastDetail]
 */
class PodcastDetailTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private lateinit var imageContentDescription: String
    private lateinit var favouriteText: String
    private lateinit var favouritedText: String

    @Before
    fun setUp() {
        composeTestRule.setContent {
            PodsTheme {
                PodcastDetail(podcastDetail = fakeDetailPodcastArticle)
            }
        }
        imageContentDescription =
            composeTestRule.activity.getString(R.string.detail_podcast_img_content_desc)
        favouriteText = composeTestRule.activity.getString(R.string.favourite_btn_text)
        favouritedText = composeTestRule.activity.getString(R.string.favourited_btn_text)
    }

    @Test
    fun podcastDetail_showsAppropriateData_onDetailPodcastInjection() {
        composeTestRule.onNodeWithText(fakeDetailPodcastArticle.title).assertIsDisplayed()
        composeTestRule.onNodeWithText(fakeDetailPodcastArticle.publisher).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription(imageContentDescription).assertIsDisplayed()
        composeTestRule.onNode(hasTextExactly(favouriteText)).assertIsDisplayed()
            .assertHasClickAction()
        composeTestRule.onNode(hasText(fakeDetailPodcastArticle.description)).assertIsDisplayed()
        composeTestRule.onNodeWithTag(PROGRESS_TAG).assertDoesNotExist()
    }
}
