package com.senijoshua.pods.data.local

import androidx.paging.PagingSource
import androidx.paging.testing.asPagingSourceFactory
import com.senijoshua.pods.data.local.podcast.PodcastDao
import com.senijoshua.pods.data.local.podcast.PodcastEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

/**
 * Fake implementation of [PodcastDao] for testing purposes.
 */
class FakePodcastDao : PodcastDao {
    // In-memory "database" against which we execute "DB" operations.
    private var podcastTable = MutableStateFlow(emptyList<PodcastEntity>())
    var shouldThrowError = false

    override suspend fun insertPodcasts(podcasts: List<PodcastEntity>) {
        podcastTable.value += podcasts
    }

    override fun getPagedPodcasts(): PagingSource<Int, PodcastEntity> {
        val podcasts = podcastTable.value
        val pagingSourceFactory = if (shouldThrowError) {
            val emptyPodcasts = mutableListOf<PodcastEntity>()
            emptyPodcasts.asPagingSourceFactory()
        } else {
            podcasts.asPagingSourceFactory()
        }
        return pagingSourceFactory()
    }

    override fun getPodcastGivenId(podcastId: String): Flow<PodcastEntity> {
        return podcastTable.map { podcasts ->
            podcasts.first {
                it.podcastId == podcastId
            }
        }
    }

    override suspend fun favouritePodcast(podcastId: String) {
        podcastTable.value = podcastTable.value.map { podcastEntity ->
            if (podcastEntity.podcastId == podcastId) {
                podcastEntity.copy(isFavourite = !podcastEntity.isFavourite)
            } else {
                podcastEntity
            }
        }
    }

    override suspend fun clearPodcasts() {
        podcastTable.value = emptyList()
    }
}
