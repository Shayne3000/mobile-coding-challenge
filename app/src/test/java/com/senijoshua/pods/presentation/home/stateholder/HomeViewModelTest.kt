package com.senijoshua.pods.presentation.home.stateholder

import androidx.paging.testing.asSnapshot
import com.senijoshua.pods.presentation.home.model.fakePodcastList
import com.senijoshua.pods.presentation.util.MainDispatcherRule
import com.senijoshua.shared_test.data.repository.FakePodcastRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelTest {
    @get:Rule(order = 0)
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: FakePodcastRepositoryImpl

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        repository = FakePodcastRepositoryImpl()
        viewModel = HomeViewModel(repository)
    }

    @Test
    fun `pagedPodcasts returns a paged list of HomePodcast on success`() = runTest {
        val job = launch {
            val pagedPodcasts = viewModel.pagedPodcasts
            val homePodcasts = pagedPodcasts.asSnapshot()
            assertFalse(homePodcasts.isEmpty())
            assertEquals(fakePodcastList, homePodcasts)
        }

        job.cancel()
    }

    @Test
    fun `pagedPodcasts returns an empty list on error`() = runTest {
        repository.shouldThrowError = true

        val job = launch {
            val pagedPodcasts = viewModel.pagedPodcasts
            val homePodcasts = pagedPodcasts.asSnapshot()
            assertTrue(homePodcasts.isEmpty())
            assertNotEquals(fakePodcastList, homePodcasts)
        }

        job.cancel()
    }
}
