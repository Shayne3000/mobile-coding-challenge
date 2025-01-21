package com.senijoshua.pods.data.local.remotekey

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

/**
 * Abstraction through which interactions with the RemoteKey table occur.
 */
@Dao
interface RemoteKeyDao {
    @Upsert
    suspend fun insertRemoteKey(remoteKeys: List<RemoteKeyEntity>)

    @Query("SELECT * FROM remote_keys ORDER BY createdAt DESC LIMIT 1")
    suspend fun getLastInsertedRemoteKey(): RemoteKeyEntity

    @Query("DELETE FROM remote_keys")
    suspend fun clearRemoteKeys()

    @Query("SELECT createdAt FROM remote_keys ORDER BY createdAt DESC LIMIT 1")
    suspend fun getCreatedTime(): Long?
}
