package com.akpdev.movies.domain.useCase

import com.akpdev.movies.domain.model.PagingMetaData
import com.akpdev.movies.domain.repository.MovieRepository
import javax.inject.Inject

class GetCachedUpcomingPagingMetadataUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    operator fun invoke(): PagingMetaData? {
        return movieRepository.getCachedUpcomingPagingMetadata()
    }
}