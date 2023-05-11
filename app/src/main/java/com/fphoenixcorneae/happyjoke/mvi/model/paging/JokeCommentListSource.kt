package com.fphoenixcorneae.happyjoke.mvi.model.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.fphoenixcorneae.happyjoke.https.jokeService
import com.fphoenixcorneae.happyjoke.mvi.model.JokeCommentListReply

/**
 * @desc：段子评论列表
 * @date：2023/05/10 16:26
 */
class JokeCommentListSource(
    private val targetJokeId: String,
) : PagingSource<Int, JokeCommentListReply.Data.Comment>() {
    override fun getRefreshKey(state: PagingState<Int, JokeCommentListReply.Data.Comment>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, JokeCommentListReply.Data.Comment> {
        return runCatching {
            val nextPage = params.key ?: 1
            val data = jokeService.getTargetJokeCommentList(targetJokeId, nextPage)?.data
            LoadResult.Page(
                data = data?.comments ?: mutableListOf(),
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if (data?.comments.isNullOrEmpty()) null else nextPage + 1,
            )
        }.getOrElse {
            LoadResult.Error(it)
        }
    }
}