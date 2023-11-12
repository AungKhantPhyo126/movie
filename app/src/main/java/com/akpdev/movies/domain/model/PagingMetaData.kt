package com.akpdev.movies.domain.model

import com.akpdev.movies.data.localDataSource.entity.PagingMetaDataEntity

data class PagingMetaData(
    val page:Int,
    val totalPages:Int
)

fun PagingMetaData.toPagingDataEntity():PagingMetaDataEntity{
    return PagingMetaDataEntity(
        page, totalPages
    )
}
fun PagingMetaDataEntity.toPagingData():PagingMetaData{
    return PagingMetaData(
        page?:0, totalPages
    )
}