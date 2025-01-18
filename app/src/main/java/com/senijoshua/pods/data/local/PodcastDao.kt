package com.senijoshua.pods.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

/**
 * Interface through which interactions with the Podcasts table occur.
 */
@Dao
interface PodcastDao {
    @Upsert
    suspend fun insertPodcasts(podcasts: List<PodcastEntity>)

    @Query("SELECT * FROM podcasts LIMIT :limit OFFSET :offset")
    fun getAllPodcasts(limit: Int, offset: Int): Flow<List<PodcastEntity>>

    @Query("SELECT * FROM podcasts WHERE id = :podcastId")
    fun getPodcastGivenId(podcastId: String): PodcastEntity

    @Query("SELECT COUNT(*) FROM podcasts")
    suspend fun getNumberOfPodcasts(): Int

    @Query("DELETE FROM podcasts")
    suspend fun clearPodcasts()
}
