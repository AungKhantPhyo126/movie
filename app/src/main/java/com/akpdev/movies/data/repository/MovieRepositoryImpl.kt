package com.akpdev.movies.data.repository

import com.akpdev.movies.common.Constants.QUERY_PAGE_SIZE
import com.akpdev.movies.common.PaginatedList
import com.akpdev.movies.data.dto.toMovie
import com.akpdev.movies.data.dto.toPaginatedList
import com.akpdev.movies.data.localDataSource.dao.MovieDao
import com.akpdev.movies.data.localDataSource.entity.PopularPagingMetaDataEntity
import com.akpdev.movies.data.localDataSource.entity.UpcomingPagingMetaDataEntity
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
            val movieList = moviedbApi.getUpcomingMovies(page).body()?.toPaginatedList(type = "upcoming")
            movieDao.upsertAll(
                movieList?.data?.mapIndexed { index, movie -> movie.toMovieEntity() }?.toSet()?.toList().orEmpty()
            )
            movieDao.insertUpcomingPagingMetaData(UpcomingPagingMetaDataEntity(page = movieList?.page?:0 , totalPages = movieList?.totalPage?:0))
            Result.success(movieList ?: PaginatedList<Movie>(emptyList(), 0, 0))
        } catch (e: HttpException) {
            Result.failure(exception = e)
        } catch (e: IOException) {
            Result.failure(exception = e)
        }
    }

    override fun getCachedUpcomingPagingMetadata(): PagingMetaData? {
        return movieDao.getUpcomingPagingMetaDataEntity()?.toPagingData()
    }

    override fun getCachedPopularPagingMetadata(): PagingMetaData? {
        return movieDao.getPopularPagingMetaDataEntity()?.toPagingData()
    }

    override suspend fun getPopularMovieList(page: Int):Result<PaginatedList<Movie>> {
        return try {
            val movieList = moviedbApi.getPopularMovies(page).body()?.toPaginatedList(type = "popular")
            movieDao.upsertAll(
                movieList?.data?.mapIndexed { index, movie -> movie.toMovieEntity() }?.toSet()?.toList().orEmpty()
            )
            movieDao.insertPopularPagingMetaData(PopularPagingMetaDataEntity(page = movieList?.page?:0 , totalPages = movieList?.totalPage?:0))
            Result.success(movieList ?: PaginatedList<Movie>(emptyList(), 0, 0))
        } catch (e: HttpException) {
            Result.failure(exception = e)
        } catch (e: IOException) {
            Result.failure(exception = e)
        }
    }

    override  fun toggleFavoriteMovie(movieId:String,isFavorite:Boolean): Result<Boolean> {
        return try {
            movieDao.toggleFavorite(movieId, favorite = isFavorite )
            Result.success(isFavorite)
        }catch (e:Exception){
            Result.failure(e)
        }
    }

    override fun fetchUpcomingMoviesFromRoom(): Flow<List<Movie>> {
        val moviesFromRoom = movieDao.getMovies("upcoming")
        return  moviesFromRoom.map {it.map { it.toMovie() }}

    }

    override fun fetchPopularMoviesFromRoom(): Flow<List<Movie>> {
        val moviesFromRoom = movieDao.getMovies("popular")
        return  moviesFromRoom.map {it.map { it.toMovie() }}
    }

    override fun fetchFavoriteMoviesFromRoom(): List<Movie> {
        return movieDao.getFavoriteMovies(favorite = true).map { it.toMovie() }.distinctBy { it.id }
    }

}