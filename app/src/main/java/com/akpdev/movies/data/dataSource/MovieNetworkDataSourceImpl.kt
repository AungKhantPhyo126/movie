package com.akpdev.movies.data.dataSource

import com.akpdev.movies.common.PaginatedList
import com.akpdev.movies.data.dto.MoviesDto
import com.akpdev.movies.data.localDataSource.dao.MovieDao
import com.akpdev.movies.data.remote.api.MoviedbApi
import com.akpdev.movies.domain.dataSource.MovieNetworkDataSource
import javax.inject.Inject

class MovieNetworkDataSourceImpl @Inject constructor(
    private val moviedbApi: MoviedbApi,
    private val movieDao: MovieDao
) : MovieNetworkDataSource {
    override suspend fun getMoviesFromNetwork(page: Int): PaginatedList<MoviesDto> {
        TODO("Not yet implemented")
    }
}