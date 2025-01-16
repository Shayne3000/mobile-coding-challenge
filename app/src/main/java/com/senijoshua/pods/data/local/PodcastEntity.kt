package com.senijoshua.pods.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "podcasts")
data class PodcastEntity(
    @PrimaryKey
    val id: String,
    val isFavourite: Boolean,
)
