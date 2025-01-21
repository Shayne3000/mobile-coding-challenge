package com.senijoshua.pods.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.senijoshua.pods.data.local.podcast.PodcastDao
import com.senijoshua.pods.data.local.podcast.PodcastEntity
import com.senijoshua.pods.data.local.remotekey.RemoteKeyDao
import com.senijoshua.pods.data.local.remotekey.RemoteKeyEntity

/**
 * Base Room Database extension for the app.
 */
@Database(version = 1, entities = [PodcastEntity::class, RemoteKeyEntity::class])
abstract class PodsDatabase : RoomDatabase() {
    abstract fun podcastDao(): PodcastDao
    abstract fun remoteKeyDao(): RemoteKeyDao

    companion object {
        const val DATABASE_NAME = "pods_db"
    }
}
