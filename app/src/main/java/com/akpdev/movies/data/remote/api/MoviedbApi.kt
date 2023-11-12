package com.akpdev.movies.data.remote.api

import com.akpdev.movies.data.dto.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviedbApi {
    @GET("upcoming")
    suspend fun getUpcomingMovies(
        @Query("page") pageNumber: Int
    ): Response<MovieResponse>

    @GET("popular")
    suspend fun getPopularMovies(
        @Query("page") pageNumber: Int
    ): Response<MovieResponse>

}