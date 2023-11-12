package com.akpdev.movies.domain.model

import com.akpdev.movies.data.localDataSource.entity.MovieEntity

data class Movie(
    val id:String,
    val title:String,
    val posterPath:String,
    val overview:String,
    val itemOrder: Int
)

fun Movie.toMovieEntity(): MovieEntity {
    return MovieEntity(
        id = id.toInt(),
        title = title,
        posterPath = posterPath,
        overview = overview,
        isFav = false,
        itemOrder = null
    )
}

fun MovieEntity.toMovie(): Movie {
    return Movie(
        id = id.toString(),
        title = title,
        posterPath = posterPath,
        overview = overview,
        itemOrder =itemOrder?:0
    )
}
