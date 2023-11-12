package com.akpdev.movies.domain.model

import com.akpdev.movies.data.localDataSource.entity.PopularPagingMetaDataEntity
import com.akpdev.movies.data.localDataSource.entity.UpcomingPagingMetaDataEntity

data class PagingMetaData(
    val page:Int,
    val totalPages:Int
)

fun PagingMetaData.toUpcomingPagingDataEntity(): UpcomingPagingMetaDataEntity {
    return UpcomingPagingMetaDataEntity(
        page, totalPages
    )
}

fun PagingMetaData.toPopularPagingDataEntity(): PopularPagingMetaDataEntity {
    return PopularPagingMetaDataEntity(
        page, totalPages
    )
}
fun UpcomingPagingMetaDataEntity.toPagingData():PagingMetaData{
    return PagingMetaData(
        page?:0, totalPages
    )
}

fun PopularPagingMetaDataEntity.toPagingData():PagingMetaData{
    return PagingMetaData(
        page?:0, totalPages
    )
}