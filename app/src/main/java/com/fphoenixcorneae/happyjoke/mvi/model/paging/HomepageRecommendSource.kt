package com.fphoenixcorneae.happyjoke.mvi.model.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.fphoenixcorneae.happyjoke.https.homepageService
import com.fphoenixcorneae.happyjoke.mvi.model.JokeListReply

/**
 * @desc：首页推荐列表数据源
 * @date：2023/04/11 15:05
 */
class HomepageRecommendSource : PagingSource<Int, JokeListReply.Data>() {
    override fun getRefreshKey(state: PagingState<Int, JokeListReply.Data>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, JokeListReply.Data> {
        return runCatching {
            val nextPage = params.key ?: 1
            val data = homepageService.homepageRecommend()?.data ?: mutableListOf()
            LoadResult.Page(
                data = data,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if (data.isEmpty()) null else nextPage + 1,
            )
        }.getOrElse {
            LoadResult.Error(it)
        }
    }
}