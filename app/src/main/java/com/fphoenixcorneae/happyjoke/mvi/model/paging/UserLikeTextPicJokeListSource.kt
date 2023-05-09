package com.fphoenixcorneae.happyjoke.mvi.model.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.fphoenixcorneae.happyjoke.https.jokeService
import com.fphoenixcorneae.happyjoke.mvi.model.JokeListReply

/**
 * @desc：用户喜欢的图文段子列表
 * @date：2023/05/09 17:08
 */
class UserLikeTextPicJokeListSource(
    private val targetUserId: String,
) : PagingSource<Int, JokeListReply.Data>() {
    override fun getRefreshKey(state: PagingState<Int, JokeListReply.Data>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, JokeListReply.Data> {
        return runCatching {
            val nextPage = params.key ?: 1
            val data = jokeService.getTargetUserLikeTextPicJokeList(
                targetUserId = targetUserId,
                page = nextPage,
            )?.data ?: mutableListOf()
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