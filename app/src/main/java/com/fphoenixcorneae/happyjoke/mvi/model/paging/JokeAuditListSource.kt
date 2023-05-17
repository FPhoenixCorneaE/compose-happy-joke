package com.fphoenixcorneae.happyjoke.mvi.model.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.fphoenixcorneae.happyjoke.https.jokeService
import com.fphoenixcorneae.happyjoke.mvi.model.JokeListReply

/**
 * @desc：审核帖子列表数据源
 * @date：2023/05/17 16:21
 */
class JokeAuditListSource(
    private val status: String,
) : PagingSource<Int, JokeListReply.Data>() {
    override fun getRefreshKey(state: PagingState<Int, JokeListReply.Data>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, JokeListReply.Data> {
        return runCatching {
            val nextPage = params.key ?: 1
            val data = jokeService.getAuditList(status = status, page = nextPage)?.data ?: mutableListOf()
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