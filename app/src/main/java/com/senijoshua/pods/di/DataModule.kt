package com.senijoshua.pods.di

import androidx.paging.ExperimentalPagingApi
import androidx.paging.RemoteMediator
import com.senijoshua.pods.data.local.podcast.PodcastEntity
import com.senijoshua.pods.data.local.util.dbcache.DbCacheLimit
import com.senijoshua.pods.data.local.util.dbcache.DbCacheLimitImpl
import com.senijoshua.pods.data.local.util.dbtransaction.PodsDbTransactionProvider
import com.senijoshua.pods.data.local.util.dbtransaction.PodsDbTransactionProviderImpl
import com.senijoshua.pods.data.repository.PodcastRemoteMediator
import com.senijoshua.pods.data.repository.PodcastRepository
import com.senijoshua.pods.data.repository.PodcastRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for provisioning implementations for the
 * data layer abstractions.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Singleton
    @Binds
    internal abstract fun providePodcastRepository(repository: PodcastRepositoryImpl): PodcastRepository

    @OptIn(ExperimentalPagingApi::class)
    @Singleton
    @Binds
    internal abstract fun provideRemoteMediator(remoteMediator: PodcastRemoteMediator): RemoteMediator<Int, PodcastEntity>

    @Singleton
    @Binds
    internal abstract fun providePodsDbTransaction(
        dbTransactionProvider: PodsDbTransactionProviderImpl
    ): PodsDbTransactionProvider

    @Singleton
    @Binds
    internal abstract fun provideDbCache(dbCacheLimitImpl: DbCacheLimitImpl): DbCacheLimit
}
