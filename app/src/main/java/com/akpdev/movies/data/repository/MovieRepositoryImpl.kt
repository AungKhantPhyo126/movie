package com.akpdev.movies.data.repository

import com.akpdev.movies.common.Constants.QUERY_PAGE_SIZE
import com.akpdev.movies.common.PaginatedList
import com.akpdev.movies.data.dto.toMovie
import com.akpdev.movies.data.dto.toPaginatedList
import com.akpdev.movies.data.localDataSource.dao.MovieDao
import com.akpdev.movies.data.localDataSource.entity.PagingMetaDataEntity
import com.akpdev.movies.data.remote.api.MoviedbApi
import com.akpdev.movies.domain.model.Movie
import com.akpdev.movies.domain.model.toMovie
import com.akpdev.movies.domain.model.toMovieEntity
import com.akpdev.movies.domain.model.toPagingData
import com.akpdev.movies.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import com.akpdev.movies.domain.model.PagingMetaData

class MovieRepositoryImpl @Inject constructor(
    private val moviedbApi: MoviedbApi,
    private val movieDao: MovieDao
) : MovieRepository {
    override suspend fun getUpcomingMovieList(
        page: Int,
    ): Result<PaginatedList<Movie>> {
        return try {
            val movieList = moviedbApi.getUpcomingMovies(page).body()?.toPaginatedList()
            movieDao.upsertAll(
                movieList?.data?.mapIndexed { index, movie -> movie.toMovieEntity() }?.toSet()?.toList().orEmpty()
            )
            movieDao.insertPagingMetaData(PagingMetaDataEntity(page = movieList?.page?:0 , totalPages = movieList?.totalPage?:0))
            Result.success(movieList ?: PaginatedList<Movie>(emptyList(), 0, 0))
        } catch (e: HttpException) {
            Result.failure(exception = e)
        } catch (e: IOException) {
            Result.failure(exception = e)
        }
    }

    override fun getCachedPagingMetadata(): PagingMetaData? {
        return movieDao.getPagingMetaData()?.toPagingData()
    }

    override suspend fun getPopularMovieList(page: Int): List<Movie> {
        return moviedbApi.getPopularMovies(page).body()?.results?.map { it.toMovie() }.orEmpty()
    }

    override suspend fun addToFavoriteMovies(movie: Movie): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override fun fetchUpcomingMoviesFromRoom(): Flow<List<Movie>> {
        val moviesFromRoom = movieDao.getAllMovies()
        return  moviesFromRoom.map {it.map { it.toMovie() }}

    }

    override suspend fun fetchPopularMoviesFromRoom(): List<Movie> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchFavoriteMoviesFromRoom(): List<Movie> {
        TODO("Not yet implemented")
    }
}