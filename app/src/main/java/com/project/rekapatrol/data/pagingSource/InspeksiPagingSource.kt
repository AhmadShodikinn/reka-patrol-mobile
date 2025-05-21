package com.project.rekapatrol.data.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.project.rekapatrol.data.response.DataItemInspeksi
import com.project.rekapatrol.network.ApiService

class InspeksiPagingSource(
    private val apiService: ApiService
): PagingSource<Int, DataItemInspeksi>() {
    override fun getRefreshKey(state: PagingState<Int, DataItemInspeksi>): Int? {
        return state.anchorPosition?.let { position ->
            state.closestPageToPosition(position)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(position)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DataItemInspeksi> {
        return try {
            val page = params.key ?: 1
            val response = apiService.getInspectionsList(
                perPage = params.loadSize,
                page = page
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