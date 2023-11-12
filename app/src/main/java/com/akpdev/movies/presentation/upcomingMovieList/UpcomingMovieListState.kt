package com.akpdev.movies.presentation.upcomingMovieList

import com.akpdev.movies.domain.model.Movie

data class UpcomingMovieListState(
    val isLoading:Boolean = false,
    val upcomingMovieList:List<Movie> = emptyList(),
    val error:String = "",
    val hasInternet:Boolean = true
)
