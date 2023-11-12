package com.akpdev.movies.domain.useCase

import com.akpdev.movies.domain.model.Movie
import com.akpdev.movies.domain.repository.MovieRepository
import javax.inject.Inject

class GetMovieDetailUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    operator fun invoke(id:Int):Movie = movieRepository.getMovieDetail(id)
}