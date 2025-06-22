package com.project.rekapatrol.data.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.project.rekapatrol.data.response.DataItemSafetyPatrols
import com.project.rekapatrol.network.ApiService

class SafetyPatrolPagingSource(
    private val apiService: ApiService,
    private val toDate: String?,
    private val fromDate: String?,
    private val isValid: String?
): PagingSource<Int, DataItemSafetyPatrols>() {
    override fun getRefreshKey(state: PagingState<Int, DataItemSafetyPatrols>): Int? {
        return state.anchorPosition?.let { position ->
            state.closestPageToPosition(position)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(position)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DataItemSafetyPatrols> {
        return try {
            val page = params.key ?: 1
            val response = apiService.getInputSafetyPatrolsList(
                relations = listOf("pic"),
                perPage = params.loadSize,
                page = page,
                sortDateBy = "updated_at",
                toDate = toDate,
                fromDate = fromDate,
                isValid = isValid
            )

            val data = response.body()?.data?.filterNotNull() ?: emptyList()

            LoadResult.Page(
                data = data,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (data.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}