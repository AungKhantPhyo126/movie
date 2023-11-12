package com.akpdev.movies.common

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay

class DefaultPaginator<Key, Item>(
    private val initialKey: Key,
    private inline val onLoadUpdated: (Boolean) -> Unit,
    private inline val onRequest: suspend (nextKey: Key) -> Result<PaginatedList<Item>>,
    private inline val getNextKey: suspend (PaginatedList<Item>) -> Key,
    private inline val onError: suspend (Throwable?) -> Unit,
    private inline val onSuccess: suspend (items: List<Item>, newKey: Key, isEndReached: Boolean) -> Unit,
) : Paginator<Key, Item> {

    private var currentKey = initialKey
    private var isMakingRequest = false

    override suspend fun loadNextItems() {
        if (isMakingRequest) {
            return
        }
        isMakingRequest = true
        onLoadUpdated(true)
        delay(2000L)
        val result = onRequest(currentKey)
        isMakingRequest = false
        val paginatedList = result.getOrElse {
            if (it.cause !is CancellationException) {
                onError(it)
                onLoadUpdated(false)
            }
            return
        }
        currentKey = getNextKey(paginatedList)
        onSuccess(paginatedList.data, currentKey, paginatedList.hasPageEndReach)
        onLoadUpdated(false)
    }

    override fun reset() {
        currentKey = initialKey
    }
}