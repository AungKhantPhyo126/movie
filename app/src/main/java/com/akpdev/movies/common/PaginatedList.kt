package com.akpdev.movies.common

data class PaginatedList<list>(
    val data: List<list>,
    val page:Int,
    val totalPage: Int
){
    val hasPageEndReach = page>=totalPage
}

