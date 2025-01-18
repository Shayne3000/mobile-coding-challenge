package com.senijoshua.pods.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity class representing a row in the Podcasts table
 */
@Entity(tableName = "podcasts")
data class PodcastEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val thumbnail: String,
    val image: String,
    val publisher: String,
    val description: String,
    @ColumnInfo(name = "is_favourite")
    val isFavourite: Boolean,
)
