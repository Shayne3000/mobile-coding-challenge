package com.senijoshua.pods.presentation.detail.stateholder

import com.senijoshua.pods.presentation.detail.model.fakeDetailPodcastArticle
import com.senijoshua.pods.presentation.detail.navigation.DetailRoute
import com.senijoshua.pods.presentation.util.MainDispatcherRule
import com.senijoshua.shared_test.data.repository.FakePodcastRepositoryImpl
import com.senijoshua.shared_test.util.SavedStateHandleRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Local unit test for the [DetailViewModel]
 */
@ExperimentalCoroutinesApi
class DetailViewModelTest {
    @get:Rule(order = 0)
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule(order = 1)
    val savedStateHandleRule = SavedStateHandleRule(DetailRoute(fakeDetailPodcastArticle.id))

    private lateinit var repository: FakePodcastRepositoryImpl
    private lateinit var vm: DetailViewModel

    @Before
    fun setUp() {
        repository = FakePodcastRepositoryImpl()
        vm = DetailViewModel(
            repository = repository,
            savedStateHandle = savedStateHandleRule.savedStateHandleMock
        )
    }

    @Test
    fun `loading state shows on init with no error`() = runTest {
        assertTrue(vm.uiState.value.isLoading)
        assertNull(vm.uiState.value.details)
        assertNull(vm.uiState.value.errorMessage)
    }

    @Test
    fun`Given a podcast id, the DB returns the appropriate Podcast on success`() = runTest {
        vm.getPodcast()

        val result = vm.uiState.value
        assertEquals(fakeDetailPodcastArticle, result.details)
        assertFalse(result.isLoading)
        assertNull(result.errorMessage)
    }

    @Test
    fun `Given a podcast id, the DB returns error on failure`() = runTest {
        repository.shouldThrowError = true

        vm.getPodcast()

        val result = vm.uiState.value
        assertEquals(repository.errorText, result.errorMessage)
        assertFalse(result.isLoading)
        assertNull(result.details)
    }

    @Test
    fun `Toggling FavoriteStatus updates the Podcast in the DB`() = runTest {
        vm.getPodcast()
        assertNotNull(vm.uiState.value.details)
        assertFalse(vm.uiState.value.details!!.isFavourite)

        vm.toggleFavouriteStatus(fakeDetailPodcastArticle.id)

        vm.getPodcast()
        assertNotNull(vm.uiState.value.details)
        assertTrue(vm.uiState.value.details!!.isFavourite)
    }
}
