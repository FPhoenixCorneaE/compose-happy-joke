package com.fphoenixcorneae.happyjoke.mvi.model.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.fphoenixcorneae.happyjoke.https.homepageService
import com.fphoenixcorneae.happyjoke.mvi.model.HomepageRecommend

/**
 * @desc：首页最新列表数据源
 * @date：2023/04/20 14:27
 */
class HomepageLatestSource : PagingSource<Int, HomepageRecommend.Data>() {
    override fun getRefreshKey(state: PagingState<Int, HomepageRecommend.Data>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, HomepageRecommend.Data> {
        return runCatching {
            val nextPage = params.key ?: 1
            LoadResult.Page(
                data = homepageService.homepageLatest()?.data ?: mutableListOf(),
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = nextPage + 1
            )
        }.getOrElse {
            LoadResult.Error(it)
        }
    }
}