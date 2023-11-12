package com.akpdev.movies.domain.repository

import com.akpdev.movies.common.PaginatedList
import com.akpdev.movies.domain.model.Movie
import com.akpdev.movies.domain.model.PagingMetaData
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getUpcomingMovieList(page:Int):Result<PaginatedList<Movie>>
    fun getCachedPagingMetadata():PagingMetaData?
    suspend fun getPopularMovieList(page: Int):List<Movie>

    suspend fun addToFavoriteMovies(movie: Movie):Result<Boolean>

    fun fetchUpcomingMoviesFromRoom():Flow<List<Movie>>
    suspend fun fetchPopularMoviesFromRoom():List<Movie>
    suspend fun fetchFavoriteMoviesFromRoom():List<Movie>
}