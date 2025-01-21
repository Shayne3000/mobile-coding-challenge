package com.senijoshua.pods.data.local.podcast

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

/**
 * Abstraction through which interactions with the Podcasts table occur.
 */
@Dao
interface PodcastDao {
    @Upsert
    suspend fun insertPodcasts(podcasts: List<PodcastEntity>)

    @Query("SELECT * FROM podcasts")
    fun getPagedPodcasts(): PagingSource<Int, PodcastEntity>

    @Query("SELECT * FROM podcasts WHERE id = :podcastId")
    fun getPodcastGivenId(podcastId: String): Flow<PodcastEntity>

    @Query("UPDATE podcasts SET is_favourite = CASE WHEN is_favourite = 1 THEN 0 ELSE 1 END WHERE id = :podcastId")
    suspend fun favouritePodcast(podcastId: String)

    @Query("DELETE FROM podcasts")
    suspend fun clearPodcasts()
}
