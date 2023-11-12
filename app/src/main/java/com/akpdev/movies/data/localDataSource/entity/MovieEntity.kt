package com.akpdev.movies.data.localDataSource.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int? = null,
    val title:String,
    val posterPath:String,
    val overview:String,
    val isFav:Boolean
)
