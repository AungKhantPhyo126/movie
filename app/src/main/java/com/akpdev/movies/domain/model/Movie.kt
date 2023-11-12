package com.akpdev.movies.domain.model

import com.akpdev.movies.data.localDataSource.entity.MovieEntity

data class Movie(
    val id:String,
    val title:String,
    val posterPath:String,
    val overview:String,
    val itemOrder: Int,
    val isFavorite:Boolean,
    val movieType:String
)

fun Movie.toMovieEntity(itemOrder: Int): MovieEntity {
    return MovieEntity(
        id = id.toInt(),
        title = title,
        posterPath = posterPath,
        overview = overview,
        isFav = isFavorite,
        itemOrder = itemOrder,
        movieType = movieType
    )
}

fun MovieEntity.toMovie(): Movie {
    return Movie(
        id = id.toString(),
        title = title,
        posterPath = posterPath,
        overview = overview,
        itemOrder =itemOrder?:0,
        isFavorite = isFav,
        movieType = movieType
    )
}
