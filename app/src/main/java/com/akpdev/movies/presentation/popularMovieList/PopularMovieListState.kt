package com.akpdev.movies.presentation.popularMovieList

import com.akpdev.movies.domain.model.Movie

data class PopularMovieListState(
    val isLoading:Boolean = false,
    val popularMovieList:List<Movie> = emptyList(),
    val error:String = "",
    val hasInternet:Boolean = true
)
