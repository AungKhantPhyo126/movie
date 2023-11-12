package com.akpdev.movies.domain.model

import com.akpdev.movies.data.localDataSource.entity.MovieEntity

data class Movie(
    val id:String,
    val title:String,
    val posterPath:String,
    val overview:String
)

fun Movie.toMovieEntity(): MovieEntity {
    return MovieEntity(
        id = id.toInt(),
        title = title,
        posterPath = posterPath,
        overview = overview,
        isFav = false
    )
}

fun MovieEntity.toMovie(): Movie {
    return Movie(
        id = id.toString(),
        title = title,
        posterPath = posterPath,
        overview = overview
    )
}
