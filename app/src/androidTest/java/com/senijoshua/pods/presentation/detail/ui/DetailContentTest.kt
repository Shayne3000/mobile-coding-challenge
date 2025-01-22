package com.senijoshua.pods.presentation.detail.ui

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.senijoshua.pods.R
import com.senijoshua.pods.presentation.components.PROGRESS_TAG
import com.senijoshua.pods.presentation.detail.model.fakeDetailPodcastArticle
import com.senijoshua.pods.presentation.detail.stateholder.DetailUiState
import com.senijoshua.pods.presentation.theme.PodsTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Instrumented unit test for testing the various
 * UI states of the Detail screen content, [DetailContent].
 */
class DetailContentTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var imageContentDescription: String
    private lateinit var backIconContentDesc: String
    private lateinit var noPodcastTest: String
    private lateinit var noPodcastContentDesc: String
    private lateinit var favouriteText: String

    @Before
    fun setUp() {
        imageContentDescription =
            composeTestRule.activity.getString(R.string.detail_podcast_img_content_desc)
        noPodcastTest = composeTestRule.activity.getString(R.string.no_podcast_detail_text)
        noPodcastContentDesc = composeTestRule.activity.getString(R.string.no_podcast_content_desc)
        backIconContentDesc = composeTestRule.activity.getString(R.string.back_content_desc)
        favouriteText = composeTestRule.activity.getString(R.string.favourite_btn_text)
    }

    @Test
    fun detailContent_showsProgressIndicator_onLoading() {
        setupDetailContent(uiState = DetailUiState(isLoading = true))

        composeTestRule.onNodeWithContentDescription(backIconContentDesc).assertIsDisplayed()
        composeTestRule.onNodeWithTag(PROGRESS_TAG).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription(noPodcastContentDesc).assertDoesNotExist()
        composeTestRule.onNodeWithText(fakeDetailPodcastArticle.title).assertDoesNotExist()
    }

    @Test
    fun detailContent_showsErrorAndEmptyScreen_onLoadError() {
        val errorMessage = "Load Error!"
        setupDetailContent(uiState = DetailUiState(errorMessage = errorMessage, isLoading = false))

        composeTestRule.onNodeWithContentDescription(backIconContentDesc).assertIsDisplayed()
        composeTestRule.onNodeWithTag(PROGRESS_TAG).assertDoesNotExist()
        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
        composeTestRule.onNodeWithText(noPodcastTest).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription(noPodcastContentDesc).assertIsDisplayed()
        composeTestRule.onNodeWithText(fakeDetailPodcastArticle.title).assertDoesNotExist()
        composeTestRule.onNodeWithText(fakeDetailPodcastArticle.description).assertDoesNotExist()
    }

    @Test
    fun detailContent_showsPodcastDetail_onLoadSuccess() {
        setupDetailContent(uiState = DetailUiState(details = fakeDetailPodcastArticle, isLoading = false))

        composeTestRule.onNodeWithContentDescription(backIconContentDesc).assertIsDisplayed()
        composeTestRule.onNodeWithText(fakeDetailPodcastArticle.title).assertIsDisplayed()
        composeTestRule.onNodeWithText(fakeDetailPodcastArticle.publisher).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription(imageContentDescription).assertIsDisplayed()
        composeTestRule.onNode(hasText(favouriteText)).assertIsDisplayed().assertHasClickAction()
        composeTestRule.onNodeWithText(fakeDetailPodcastArticle.description).assertIsDisplayed()
        composeTestRule.onNodeWithTag(PROGRESS_TAG).assertDoesNotExist()
    }

    private fun setupDetailContent(uiState: DetailUiState) {
        composeTestRule.setContent {
            PodsTheme {
                DetailContent(
                    uiState = uiState
                )
            }
        }
    }
}


