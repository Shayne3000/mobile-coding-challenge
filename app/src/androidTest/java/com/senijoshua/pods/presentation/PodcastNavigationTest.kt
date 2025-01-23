package com.senijoshua.pods.presentation

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.senijoshua.pods.R
import com.senijoshua.pods.data.repository.PodcastRepository
import com.senijoshua.pods.di.DataModule
import com.senijoshua.pods.presentation.detail.model.DetailPodcast
import com.senijoshua.pods.presentation.detail.model.fakeDetailPodcastArticle
import com.senijoshua.pods.presentation.home.model.HomePodcast
import com.senijoshua.pods.presentation.home.model.fakePodcastList
import com.senijoshua.pods.presentation.home.ui.LIST_TAG
import com.senijoshua.pods.presentation.theme.PodsTheme
import com.senijoshua.shared_test.data.repository.FakePodcastRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Singleton

/**
 * App-level test that verifies the transition
 * from the HomeScreen to the DetailScreen
 */
@UninstallModules(DataModule::class)
@HiltAndroidTest
class PodcastNavigationTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<PodsActivity>()

    private lateinit var homePodcast: List<HomePodcast>
    private lateinit var detailPodcast: DetailPodcast
    private lateinit var homeTitle: String
    private lateinit var thumbnailContentDescription: String
    private lateinit var imageContentDescription: String
    private lateinit var backIconContentDesc: String
    private lateinit var favouriteText: String

    @Before
    fun setUp() {
        hiltRule.inject()
        composeTestRule.activity.setContent {
            PodsTheme {
                PodsRoot()
            }
        }
        homePodcast = fakePodcastList
        detailPodcast = fakeDetailPodcastArticle
        homeTitle = composeTestRule.activity.getString(R.string.home_title)
        thumbnailContentDescription =
            composeTestRule.activity.getString(R.string.podcast_item_thumbnail_content_desc)
        imageContentDescription =
            composeTestRule.activity.getString(R.string.detail_podcast_img_content_desc)
        backIconContentDesc = composeTestRule.activity.getString(R.string.back_content_desc)
        favouriteText = composeTestRule.activity.getString(R.string.favourite_btn_text)
    }

    @Test
    fun podsRoot_showsHomePodcastList_clicksItem_showsPodcastDetails_returnsHome() {
        composeTestRule.onNodeWithText(homeTitle).assertIsDisplayed()
        composeTestRule.onNodeWithTag(LIST_TAG).assertIsDisplayed()
        composeTestRule.onAllNodesWithContentDescription(thumbnailContentDescription).onFirst().assertIsDisplayed()
        composeTestRule.onNodeWithText(homePodcast[7].title).performScrollTo().performClick()

        composeTestRule.onNodeWithText(homeTitle).assertDoesNotExist()
        composeTestRule.onNodeWithContentDescription(backIconContentDesc).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription(imageContentDescription).assertIsDisplayed()
        composeTestRule.onNodeWithText(fakeDetailPodcastArticle.title).assertIsDisplayed()
        composeTestRule.onNodeWithText(fakeDetailPodcastArticle.description).assertIsDisplayed()
        composeTestRule.onNodeWithText(favouriteText).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription(backIconContentDesc).performClick()
        composeTestRule.onNodeWithText(homeTitle).assertExists()
        composeTestRule.onNodeWithContentDescription(backIconContentDesc).assertDoesNotExist()
    }

    /**
     * Replace the PodcastRepositoryImpl binding with
     * [FakePodcastRepositoryImpl] as the implementation
     * of [PodcastRepository] within this test class
     */
    @Module
    @InstallIn(SingletonComponent::class)
    abstract class FakeDataModule {

        @Singleton
        @Binds
        abstract fun bindPodcastRepository(fakePodcastRepositoryImpl: FakePodcastRepositoryImpl): PodcastRepository
    }
}
