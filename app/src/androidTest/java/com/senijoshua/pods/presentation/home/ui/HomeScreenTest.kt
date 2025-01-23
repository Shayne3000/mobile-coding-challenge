package com.senijoshua.pods.presentation.home.ui

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertAll
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnyChild
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.senijoshua.pods.R
import com.senijoshua.pods.presentation.PodsActivity
import com.senijoshua.pods.presentation.components.PROGRESS_TAG
import com.senijoshua.pods.presentation.home.model.HomePodcast
import com.senijoshua.pods.presentation.home.model.fakePodcastList
import com.senijoshua.pods.presentation.home.stateholder.HomeViewModel
import com.senijoshua.pods.presentation.theme.PodsTheme
import com.senijoshua.shared_test.data.repository.FakePodcastRepositoryImpl
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Tests the interaction of the [HomeScreen] with the
 * [HomeViewModel] in loading paged [HomePodcast] data.
 */
@HiltAndroidTest
class HomeScreenTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<PodsActivity>()

    private lateinit var homeTitle: String
    private lateinit var thumbnailContentDescription: String

    private lateinit var repository: FakePodcastRepositoryImpl

    @Before
    fun setUp() {
        repository = FakePodcastRepositoryImpl()
        homeTitle = composeTestRule.activity.getString(R.string.home_title)
        thumbnailContentDescription =
            composeTestRule.activity.getString(R.string.podcast_item_thumbnail_content_desc)
    }

    @Test
    fun homeContent_showsProgressIndicator_onEmptyPagingData() {
        repository.shouldThrowError = true

        setupHomeScreen()

        composeTestRule.onNodeWithText(homeTitle).assertIsDisplayed()
        composeTestRule.onNodeWithTag(PROGRESS_TAG).assertIsDisplayed()
        composeTestRule.onNode(hasAnyChild(hasText(fakePodcastList[0].title))).assertDoesNotExist()
    }

    @Test
    fun homeContent_showsHomePodcastList_onPagingDataLoaded() {
        setupHomeScreen()

        composeTestRule.onNodeWithText(homeTitle).assertIsDisplayed()
        composeTestRule.onNodeWithTag(PROGRESS_TAG).assertDoesNotExist()
        composeTestRule.onNodeWithTag(LIST_TAG).assertIsDisplayed()
        composeTestRule.onNodeWithTag(LIST_TAG).onChildren().assertAll(hasClickAction())
        val childrenSize = composeTestRule.onNodeWithTag(LIST_TAG).onChildren().fetchSemanticsNodes().size
        assertTrue(childrenSize >= 5)
    }

    private fun setupHomeScreen() {
        composeTestRule.activity.setContent {
            PodsTheme {
                HomeScreen (
                    vm = HomeViewModel(
                        repository = repository,
                    )
                )
            }
        }
    }
}
