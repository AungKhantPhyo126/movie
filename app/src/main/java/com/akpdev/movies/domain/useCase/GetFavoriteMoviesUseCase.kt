package com.akpdev.movies.domain.useCase

import com.akpdev.movies.domain.model.Movie
import com.akpdev.movies.domain.repository.MovieRepository
import javax.inject.Inject

class GetFavoriteMoviesUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    operator fun invoke():List<Movie> = movieRepository.fetchFavoriteMoviesFromRoom()
}