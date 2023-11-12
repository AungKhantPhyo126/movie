package com.akpdev.movies.data.dto

import com.akpdev.movies.common.PaginatedList
import com.akpdev.movies.domain.model.Movie
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json

data class MoviesDto(
    val id:String?,
    val title:String?,
    @Json(name = "poster_path")
    val posterPath:String?,
    val overview:String?
)
data class MovieResponse(
    val page:Int,
    val results:List<MoviesDto>,
    @Json(name = "total_pages")
    val totalPages:Int?
)

fun MovieResponse.toPaginatedList(type:String):PaginatedList<Movie>{
    return PaginatedList<Movie>(
        data = results.map { it.toMovie(type) },
        page =  page,
        totalPage = totalPages ?: 0
    )
}

fun MoviesDto.toMovie(type:String):Movie{
    return Movie(
        id = id.orEmpty(),
        title = title.orEmpty(),
        posterPath = posterPath.orEmpty(),
        overview = overview.orEmpty(),
        itemOrder = 0,
        isFavorite = false,
        movieType = type
    )
}

