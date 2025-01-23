package com.senijoshua.pods.presentation.detail.ui

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.senijoshua.pods.R
import com.senijoshua.pods.presentation.PodsActivity
import com.senijoshua.pods.presentation.components.PROGRESS_TAG
import com.senijoshua.pods.presentation.detail.model.fakeDetailPodcastArticle
import com.senijoshua.pods.presentation.detail.navigation.DetailRoute
import com.senijoshua.pods.presentation.detail.stateholder.DetailViewModel
import com.senijoshua.pods.presentation.theme.PodsTheme
import com.senijoshua.shared_test.data.repository.FakePodcastRepositoryImpl
import com.senijoshua.shared_test.util.SavedStateHandleRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Tests the interaction of the [DetailScreen] with the
 * [DetailViewModel] in loading and toggling podcast detail functionality.
 */
@HiltAndroidTest
class DetailScreenTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<PodsActivity>()

    @get:Rule(order = 2)
    val savedStateHandleRule = SavedStateHandleRule(DetailRoute(fakeDetailPodcastArticle.id))

    // Resources
    private lateinit var imageContentDescription: String
    private lateinit var backIconContentDesc: String
    private lateinit var noPodcastText: String
    private lateinit var noPodcastContentDesc: String
    private lateinit var favouriteText: String
    private lateinit var favouritedText: String

    // Dependencies
    private lateinit var repository: FakePodcastRepositoryImpl

    @Before
    fun setUp() {
        hiltRule.inject()
        repository = FakePodcastRepositoryImpl()
        imageContentDescription =
            composeTestRule.activity.getString(R.string.detail_podcast_img_content_desc)
        noPodcastText = composeTestRule.activity.getString(R.string.no_podcast_detail_text)
        noPodcastContentDesc = composeTestRule.activity.getString(R.string.no_podcast_content_desc)
        backIconContentDesc = composeTestRule.activity.getString(R.string.back_content_desc)
        favouriteText = composeTestRule.activity.getString(R.string.favourite_btn_text)
        favouritedText = composeTestRule.activity.getString(R.string.favourited_btn_text)
    }

    @Test
    fun detailScreen_LoadsCorrectPodcast_onSuccess() {
        setupDetailScreen()

        composeTestRule.onNodeWithContentDescription(backIconContentDesc).assertIsDisplayed()
        composeTestRule.onNodeWithText(fakeDetailPodcastArticle.title).assertIsDisplayed()
        composeTestRule.onNodeWithText(fakeDetailPodcastArticle.description).assertIsDisplayed()
        composeTestRule.onNodeWithText(favouriteText).assertIsDisplayed()
        composeTestRule.onNodeWithTag(PROGRESS_TAG).assertIsNotDisplayed()
        composeTestRule.onNodeWithText(noPodcastText).assertDoesNotExist()
    }

    @Test
    fun detailScreen_showsError_onLoadFailure() {
        repository.shouldThrowError = true

        setupDetailScreen()

        composeTestRule.onNodeWithContentDescription(backIconContentDesc).assertIsDisplayed()
        composeTestRule.onNodeWithText(repository.errorText).assertIsDisplayed()
        composeTestRule.onNodeWithText(noPodcastText).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription(noPodcastContentDesc).assertIsDisplayed()
        composeTestRule.onNodeWithText(fakeDetailPodcastArticle.title).assertDoesNotExist()
        composeTestRule.onNodeWithText(fakeDetailPodcastArticle.description).assertDoesNotExist()
    }

    private fun setupDetailScreen() {
        composeTestRule.activity.setContent {
            PodsTheme {
                DetailScreen(
                    vm = DetailViewModel(
                        repository = repository,
                        savedStateHandle = savedStateHandleRule.savedStateHandleMock
                    )
                )
            }
        }
    }
}
