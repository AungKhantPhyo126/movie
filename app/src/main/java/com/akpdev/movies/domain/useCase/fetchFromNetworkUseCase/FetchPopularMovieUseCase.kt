package com.akpdev.movies.domain.useCase.fetchFromNetworkUseCase

import com.akpdev.movies.common.PaginatedList
import com.akpdev.movies.domain.model.Movie
import com.akpdev.movies.domain.repository.MovieRepository
import javax.inject.Inject

class FetchPopularMovieUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(page: Int): Result<PaginatedList<Movie>> {
        return movieRepository.getPopularMovieList(page)
    }
}