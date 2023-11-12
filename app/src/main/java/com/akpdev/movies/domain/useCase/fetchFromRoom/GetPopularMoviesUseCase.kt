package com.akpdev.movies.domain.useCase.fetchFromRoom

import com.akpdev.movies.common.Resource
import com.akpdev.movies.domain.model.Movie
import com.akpdev.movies.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetPopularMoviesUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    operator fun invoke(): Flow<List<Movie>> {
        return movieRepository.fetchPopularMoviesFromRoom()
    }
}