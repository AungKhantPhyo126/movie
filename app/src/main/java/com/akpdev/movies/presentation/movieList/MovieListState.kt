package com.akpdev.movies.presentation.movieList

import com.akpdev.movies.domain.model.Movie

data class MovieListState(
    val isLoading:Boolean = false,
    val upcomingMovieList:List<Movie> = emptyList(),
    val popularMovieList:List<Movie> = emptyList(),
    val error:String = "",
    val hasInternet:Boolean = true
)
