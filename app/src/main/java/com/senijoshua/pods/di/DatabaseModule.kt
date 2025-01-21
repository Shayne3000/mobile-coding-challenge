package com.senijoshua.pods.di

import android.content.Context
import androidx.room.Room
import com.senijoshua.pods.data.local.podcast.PodcastDao
import com.senijoshua.pods.data.local.PodsDatabase
import com.senijoshua.pods.data.local.remotekey.RemoteKeyDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for provisioning DB-interaction-specific elements for injection
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providePodsDatabase(@ApplicationContext context: Context): PodsDatabase {
        return Room.databaseBuilder(
            context,
            PodsDatabase::class.java,
            PodsDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun providePodcastDao(db: PodsDatabase): PodcastDao = db.podcastDao()

    @Provides
    @Singleton
    fun provideRemoteKeyDao(db: PodsDatabase): RemoteKeyDao = db.remoteKeyDao()
}
