package com.senijoshua.pods.di

import android.content.Context
import androidx.room.Room
import com.senijoshua.pods.data.local.PodcastDao
import com.senijoshua.pods.data.local.PodsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

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
    fun provideArticleDao(db: PodsDatabase): PodcastDao = db.podcastDao()
}
