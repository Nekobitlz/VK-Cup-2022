package ru.vk.cup.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.vk.cup.NavigationType

class InteractiveItemDataSource(
    private val repository: InteractiveItemRepository,
    private val type: NavigationType,
) : PagingSource<Int, InteractiveItem>() {

    override fun getRefreshKey(state: PagingState<Int, InteractiveItem>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, InteractiveItem> {
        val page = params.key ?: 0
        val size = params.loadSize
        val from = page * size
        val data = when(type) {
            NavigationType.ALL_ITEMS -> repository.generateItems(from = from, size = size)
            NavigationType.POLL -> repository.generatePolls(from = from, size = size)
            NavigationType.RATING -> repository.generateRatings(from = from, size = size)
            NavigationType.GAPS_WITH_ANSWERS -> repository.generateGapsWithAnswers(from = from, size = size)
            NavigationType.GAPS_WITH_FIELDS -> repository.generateGapsWithFields(from = from, size = size)
            NavigationType.COLUMN -> repository.generateColumns(from = from, size = size)
        }
        return LoadResult.Page(
            data = data,
            prevKey = if (page == 0) null else page - 1,
            nextKey = if (data.isEmpty()) null else page + 1
        )
    }
}