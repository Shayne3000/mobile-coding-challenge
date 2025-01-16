package com.senijoshua.pods.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Base Room Database extension for the app.
 */
@Database(version = 1, entities = [PodcastEntity::class])
abstract class PodsDatabase : RoomDatabase() {
    abstract fun podcastDao(): PodcastDao

    companion object {
        const val DATABASE_NAME = "pods_db"
    }
}
