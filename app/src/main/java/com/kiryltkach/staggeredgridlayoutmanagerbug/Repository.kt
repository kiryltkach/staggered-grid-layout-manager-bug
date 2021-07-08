package com.kiryltkach.staggeredgridlayoutmanagerbug

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.flow.Flow

private const val PAGE_SIZE = 50

/**
 * Sample repository that provides both pages of consecutive integer numbers and empty pages
 */
object Repository {

    // Returns paging data flow of consecutive integer numbers
    fun getNumbers(): Flow<PagingData<Int>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { NumbersPagingSource() }
        ).flow
    }

    // Returns empty paging data flow
    fun getEmptyNumbers(): Flow<PagingData<Int>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { NumbersPagingSource(empty = true) }
        ).flow
    }
}

/**
 * @param empty if set to true returns empty list in load result without next page
 */
private class NumbersPagingSource(private val empty: Boolean = false) : PagingSource<Int, Int>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Int> {
        val key = params.key ?: 0
        return LoadResult.Page(
            if (empty) emptyList() else IntRange(key * PAGE_SIZE + 1, (key + 1) * PAGE_SIZE).toList(),
            null, // Paging only forward
            if (empty) null else (key + 1)
        )
    }

    override fun getRefreshKey(state: PagingState<Int, Int>): Int? = null
}