package com.senijoshua.pods.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Hilt module for provisioning [CoroutineDispatcher]s for injection.
 */
@Module
@InstallIn(SingletonComponent::class)
object CoroutinesModule {
    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
}
