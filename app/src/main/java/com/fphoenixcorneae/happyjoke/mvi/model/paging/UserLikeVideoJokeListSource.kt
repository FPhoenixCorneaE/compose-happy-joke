package com.fphoenixcorneae.happyjoke.mvi.model.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.fphoenixcorneae.happyjoke.https.jokeService
import com.fphoenixcorneae.happyjoke.mvi.model.VideoListReply

/**
 * @desc：用户喜欢的视频列表
 * @date：2023/05/10 10:25
 */
class UserLikeVideoJokeListSource(
    private val targetUserId: String,
) : PagingSource<Int, VideoListReply.Data>() {
    override fun getRefreshKey(state: PagingState<Int, VideoListReply.Data>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, VideoListReply.Data> {
        return runCatching {
            val nextPage = params.key ?: 1
            val data = jokeService.getTargetUserLikeVideoJokeList(
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