package com.fphoenixcorneae.happyjoke.mvi.model.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.fphoenixcorneae.happyjoke.https.homepageService
import com.fphoenixcorneae.happyjoke.mvi.model.JokeListReply

/**
 * @desc：首页关注的用户发布的段子列表数据源
 * @date：2023/04/11 15:05
 */
class HomepageAttentionListSource : PagingSource<Int, JokeListReply.Data>() {
    override fun getRefreshKey(state: PagingState<Int, JokeListReply.Data>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, JokeListReply.Data> {
        return runCatching {
            val nextPage = params.key ?: 1
            val data = homepageService.homepageAttentionList(nextPage)?.data ?: mutableListOf()
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