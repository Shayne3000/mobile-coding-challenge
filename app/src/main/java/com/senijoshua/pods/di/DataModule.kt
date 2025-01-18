package com.senijoshua.pods.di

import com.senijoshua.pods.data.repository.PodcastRepository
import com.senijoshua.pods.data.repository.PodcastRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Singleton
    @Binds
    internal abstract fun providePodcastRepository(repository: PodcastRepositoryImpl): PodcastRepository
}
