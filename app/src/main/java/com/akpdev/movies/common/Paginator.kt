package com.akpdev.movies.common

interface Paginator<Key,Item>{
    suspend fun loadNextItems()
    fun reset()
}