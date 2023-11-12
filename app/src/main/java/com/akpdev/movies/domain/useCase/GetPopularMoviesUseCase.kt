package com.akpdev.movies.domain.useCase

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
    operator fun invoke(page:Int): Flow<Resource<List<Movie>>> = flow {
        try {
            emit(Resource.Loading())
            val popularMovieList = movieRepository.getPopularMovieList(page)
            emit(Resource.Success(popularMovieList))
        }catch (e: HttpException){
            emit(Resource.Error(e.localizedMessage ?: "Unexpected Error Occurred"))
        }catch (e: IOException){
            emit(Resource.Error("Cannot Connect to Server.Please check your connection"))
        }
    }
}