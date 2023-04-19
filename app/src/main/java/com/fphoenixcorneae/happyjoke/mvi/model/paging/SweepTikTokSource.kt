package com.fphoenixcorneae.happyjoke.mvi.model.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.fphoenixcorneae.happyjoke.https.apiService
import com.fphoenixcorneae.happyjoke.mvi.model.SweepTikTokVideo

/**
 * @desc：划一划页面的推荐列表数据源
 * @date：2023/04/11 15:05
 */
class SweepTikTokSource : PagingSource<Int, SweepTikTokVideo.Data>() {
    override fun getRefreshKey(state: PagingState<Int, SweepTikTokVideo.Data>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SweepTikTokVideo.Data> {
        return runCatching {
            val nextPage = params.key ?: 1
            LoadResult.Page(
                data = apiService.sweepTikTokVideo()?.data ?: mutableListOf(),
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = nextPage + 1
            )
        }.getOrElse {
            LoadResult.Error(it)
        }
    }
}