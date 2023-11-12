package com.akpdev.movies.domain.repository

import com.akpdev.movies.common.PaginatedList
import com.akpdev.movies.domain.model.Movie
import com.akpdev.movies.domain.model.PagingMetaData
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getUpcomingMovieList(page:Int):Result<PaginatedList<Movie>>
    fun getCachedUpcomingPagingMetadata():PagingMetaData?
    fun getCachedPopularPagingMetadata():PagingMetaData?
    suspend fun getPopularMovieList(page: Int):Result<PaginatedList<Movie>>

    fun toggleFavoriteMovie(movieId: String,isFavorite:Boolean):Result<Boolean>

    fun fetchUpcomingMoviesFromRoom():Flow<List<Movie>>
    fun fetchPopularMoviesFromRoom():Flow<List<Movie>>
    fun fetchFavoriteMoviesFromRoom():List<Movie>
}