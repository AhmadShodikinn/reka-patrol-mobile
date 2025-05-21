package com.project.rekapatrol.data.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.project.rekapatrol.data.response.DataItemCriterias
import com.project.rekapatrol.network.ApiService

class CriteriasPagingSource(
    private val apiService: ApiService,
    private val criteriaType: String,
    private val locationId: Int
): PagingSource<Int, DataItemCriterias>() {
    override fun getRefreshKey(state: PagingState<Int, DataItemCriterias>): Int? {
        return state.anchorPosition?.let { position ->
            state.closestPageToPosition(position)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(position)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DataItemCriterias> {
        return try {
            val page = params.key ?: 1
            val response = apiService.getCriterias(
                perPage = params.loadSize,
                page = page,
                criteriaType = criteriaType,
                locationId = locationId,
            )

            val data = response.body()?.data?.filterNotNull() ?: emptyList()

            LoadResult.Page(
                data = data,
                prevKey = if (page == 1) null else page -1,
                nextKey = if (data.isEmpty()) null else page +1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}