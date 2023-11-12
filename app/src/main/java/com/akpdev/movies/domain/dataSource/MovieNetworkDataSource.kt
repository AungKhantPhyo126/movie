package com.akpdev.movies.domain.dataSource

import com.akpdev.movies.common.PaginatedList
import com.akpdev.movies.data.dto.MoviesDto
import com.akpdev.movies.domain.model.Movie

interface MovieNetworkDataSource {
    suspend fun getMoviesFromNetwork(page:Int):PaginatedList<MoviesDto>
}