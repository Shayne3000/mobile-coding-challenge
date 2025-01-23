@file:OptIn(ExperimentalCoroutinesApi::class, ExperimentalPagingApi::class)

package com.senijoshua.pods.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.testing.asSnapshot
import com.senijoshua.pods.data.local.FakePodcastDao
import com.senijoshua.pods.data.remote.FakePodcastApi
import com.senijoshua.pods.data.util.toDetailPodcast
import com.senijoshua.pods.data.util.toHomePodcast
import com.senijoshua.pods.data.util.toLocal
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class PodcastRepositoryImplTest {
    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var localDataSource: FakePodcastDao
    private lateinit var remoteDataSource: FakePodcastApi
    private lateinit var remoteMediator: FakePodcastRemoteMediator

    private lateinit var repository: PodcastRepositoryImpl

    @Before
    fun setUp() {
        localDataSource = FakePodcastDao()
        remoteDataSource = FakePodcastApi()
        remoteMediator = FakePodcastRemoteMediator(
            localDataSource = localDataSource,
            remoteDataSource = remoteDataSource
        )
        repository = PodcastRepositoryImpl(
            local = localDataSource,
            remoteMediator = remoteMediator,
            dispatcher = testDispatcher
        )
    }

    @Test
    fun `getPagedPodcasts returns paged list of HomePodcast on successful request`() = runTest() {
        val podcastEntityList = remoteDataSource.remotePodcastList.toLocal()
        localDataSource.insertPodcasts(podcastEntityList)

        val expectedList = podcastEntityList.toHomePodcast()

        val result = repository.getPagedPodcasts()
        val homePodcastList = result.asSnapshot()
        assertEquals(expectedList.size, homePodcastList.size)
        assertEquals(expectedList[0].id, homePodcastList[0].id)
    }

    @Test
    fun `getPagedPodcasts returns empty list of HomePodcast on error`() = runTest() {
        localDataSource.shouldThrowError = true

        val result = repository.getPagedPodcasts()

        val homePodcastList = result.asSnapshot()

        assertEquals(0, homePodcastList.size)
    }

    @Test(expected = Throwable::class)
    fun `getPagedPodcasts throws error on network load error`() = runTest {
        remoteMediator.shouldThrowError = true

        val result = repository.getPagedPodcasts()

        val homePodcastList = result.asSnapshot()

        assertEquals(0, homePodcastList.size)
    }

    @Test
    fun `getPodcastGivenId returns DetailPodcast on success`() = runTest {
        val podcastEntityList = remoteDataSource.remotePodcastList.toLocal()
        localDataSource.insertPodcasts(podcastEntityList)

        val result = repository.getPodcastGivenId(remoteDataSource.remotePodcastList[1].id).first()

        assertTrue(result.isSuccess)
        assertFalse(result.isFailure)
        val podcast = result.getOrNull()
        assertNotNull(podcast)
        assertEquals(remoteDataSource.remotePodcastList[1].toLocal().toDetailPodcast(), podcast)
    }

    @Test
    fun `getPodcastGivenId returns error on error`() = runTest {
        val result = repository.getPodcastGivenId(remoteDataSource.remotePodcastList[1].id).first()

        assertTrue(result.isFailure)
        assertFalse(result.isSuccess)
        val error = result.exceptionOrNull()
        assertNotNull(error)
    }

    @Test
    fun `toggleFavouriteStatus updates podcast favourite status`() = runTest {
        val podcastEntityList = remoteDataSource.remotePodcastList.toLocal()
        localDataSource.insertPodcasts(podcastEntityList)

        val podcastId = remoteDataSource.remotePodcastList[1].id

        val notFavouritedResult = repository.getPodcastGivenId(podcastId).first()

        val detailPodcast = notFavouritedResult.getOrNull()
        assertEquals(false, detailPodcast?.isFavourite)

        repository.togglePodcastFavouriteStatus(podcastId)

        val favouritedResult = repository.getPodcastGivenId(podcastId).first()

        assertTrue(favouritedResult.isSuccess)
        val favouritedPodcast = favouritedResult.getOrNull()
        assertEquals(true, favouritedPodcast?.isFavourite)
    }

    @After
    fun tearDown() = runTest(testDispatcher) {
        localDataSource.clearPodcasts()
    }
}
