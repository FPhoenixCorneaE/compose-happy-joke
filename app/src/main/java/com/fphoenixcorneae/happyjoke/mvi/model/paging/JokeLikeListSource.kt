package com.fphoenixcorneae.happyjoke.mvi.model.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.fphoenixcorneae.happyjoke.https.jokeService
import com.fphoenixcorneae.happyjoke.mvi.model.JokeLikeListReply

/**
 * @desc：段子点赞列表
 * @date：2023/05/10 17:07
 */
class JokeLikeListSource(
    private val targetJokeId: String,
) : PagingSource<Int, JokeLikeListReply.Data>() {
    override fun getRefreshKey(state: PagingState<Int, JokeLikeListReply.Data>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, JokeLikeListReply.Data> {
        return runCatching {
            val nextPage = params.key ?: 1
            val data = jokeService.getTargetJokeLikeList(targetJokeId, nextPage)?.data ?: mutableListOf()
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