package com.akpdev.movies.data.localDataSource.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey
    val id:Int,
    val title:String,
    val posterPath:String,
    val overview:String,
    val isFav:Boolean,
    val itemOrder:Int,
    val movieType:String
)
