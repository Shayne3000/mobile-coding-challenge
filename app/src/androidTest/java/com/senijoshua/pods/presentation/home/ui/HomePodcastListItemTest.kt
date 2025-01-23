package com.senijoshua.pods.presentation.home.ui

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import com.senijoshua.pods.R
import com.senijoshua.pods.presentation.home.model.fakePodcastList
import com.senijoshua.pods.presentation.theme.PodsTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Instrumented unit test for the [HomePodcastListItem].
 */
class HomePodcastListItemTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var favouriteText: String
    private lateinit var favouritedText: String
    private lateinit var thumbnailContentDescription: String

    @Before
    fun setUp() {
        favouriteText = composeTestRule.activity.getString(R.string.favourite_btn_text)
        favouritedText = composeTestRule.activity.getString(R.string.favourited_btn_text)
        thumbnailContentDescription =
            composeTestRule.activity.getString(R.string.podcast_item_thumbnail_content_desc)
    }

    @Test
    fun homeListItem_showsCorrectHomePodcastData_onLoad() {
        val favouritedPodcast = fakePodcastList[0]
        composeTestRule.setContent {
            PodsTheme {
                HomePodcastListItem(podcast = favouritedPodcast)
            }
        }
        composeTestRule.onNode(hasAnyDescendant(hasClickAction())).assertExists()
        composeTestRule.onNodeWithContentDescription(thumbnailContentDescription)
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(favouritedPodcast.title).assertIsDisplayed()
        composeTestRule.onNodeWithText(favouritedPodcast.publisher).assertIsDisplayed()
        composeTestRule.onNodeWithText(favouritedText).assertExists()
    }

    @Test
    fun homeListItem_doesNotShowFavouriteLabel_IfNotFavourite() {
        val unfavouritedPodcast = fakePodcastList[1]

        composeTestRule.setContent {
            PodsTheme {
                HomePodcastListItem(podcast = unfavouritedPodcast)
            }
        }
        composeTestRule.onNode(hasAnyDescendant(hasClickAction())).assertExists()
        composeTestRule.onNodeWithContentDescription(thumbnailContentDescription)
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(unfavouritedPodcast.title).assertIsDisplayed()
        composeTestRule.onNodeWithText(unfavouritedPodcast.publisher).assertIsDisplayed()
        composeTestRule.onNodeWithText(favouritedText).assertDoesNotExist()
    }
}

