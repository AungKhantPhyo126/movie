package com.akpdev.movies.data.localDataSource.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UpcomingPagingMetaDataEntity(
    @PrimaryKey(autoGenerate = true)
    val page:Int?,
    val totalPages:Int
)

@Entity
data class PopularPagingMetaDataEntity(
    @PrimaryKey(autoGenerate = true)
    val page:Int?,
    val totalPages:Int
)
