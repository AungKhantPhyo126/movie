package com.akpdev.movies.domain.useCase

import com.akpdev.movies.domain.model.Movie
import com.akpdev.movies.domain.repository.MovieRepository
import javax.inject.Inject

class ToggleFavoriteMovieUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    operator fun invoke(movieId:String , isFavorite:Boolean):Result<Boolean> = movieRepository.toggleFavoriteMovie(movieId, isFavorite)
}