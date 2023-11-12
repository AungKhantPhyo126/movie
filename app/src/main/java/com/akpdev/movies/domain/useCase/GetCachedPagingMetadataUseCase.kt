package com.akpdev.movies.domain.useCase

import com.akpdev.movies.common.PaginatedList
import com.akpdev.movies.domain.model.Movie
import com.akpdev.movies.domain.model.PagingMetaData
import com.akpdev.movies.domain.repository.MovieRepository
import javax.inject.Inject

class GetCachedPagingMetadataUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    operator fun invoke(): PagingMetaData? {
        return movieRepository.getCachedPagingMetadata()
    }
}