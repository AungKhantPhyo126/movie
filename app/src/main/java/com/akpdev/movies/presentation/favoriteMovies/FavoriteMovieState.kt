package com.akpdev.movies.presentation.favoriteMovies

import com.akpdev.movies.domain.model.Movie

data class FavoriteMovieState(
    val favoriteMovies:List<Movie> = emptyList()
)