package com.fphoenixcorneae.happyjoke.https.service

import com.fphoenixcorneae.happyjoke.mvi.model.BaseReply
import com.fphoenixcorneae.happyjoke.mvi.model.JokeListReply
import com.fphoenixcorneae.happyjoke.mvi.model.LikeListReply
import com.fphoenixcorneae.happyjoke.mvi.model.VideoListReply
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * @desc：接口地址：http://tools.cretinzp.com/jokes/doc.html
 * @date：2023/05/05 17:13
 */
interface JokeService {
    /**
     * 获取指定用户图文段子列表
     * @param targetUserId 目标用户id
     */
    @FormUrlEncoded
    @POST("/jokes/jokes/text_pic_jokes/list")
    suspend fun getTargetUserTextPicJokeList(
        @Field("targetUserId") targetUserId: String,
        @Field("page") page: Int,
    ): JokeListReply?

    /**
     * 获取指定用户喜欢的图文段子列表
     * @param targetUserId 目标用户id
     */
    @FormUrlEncoded
    @POST("/jokes/jokes/text_pic_jokes/like/list")
    suspend fun getTargetUserLikeTextPicJokeList(
        @Field("targetUserId") targetUserId: String,
        @Field("page") page: Int,
    ): JokeListReply?

    /**
     * 获取指定用户所有视频列表
     * @param targetUserId 目标用户id
     */
    @FormUrlEncoded
    @POST("/jokes/jokes/video/list")
    suspend fun getTargetUserVideoJokeList(
        @Field("targetUserId") targetUserId: String,
        @Field("page") page: Int,
    ): VideoListReply?

    /**
     * 获取指定用户喜欢的视频列表
     * @param targetUserId 目标用户id
     */
    @FormUrlEncoded
    @POST("/jokes/jokes/video/like/list")
    suspend fun getTargetUserLikeVideoJokeList(
        @Field("targetUserId") targetUserId: String,
        @Field("page") page: Int,
    ): VideoListReply?

    /**
     * 获取指定id对应视频列表
     * @param ids 段子id列表，中间用,分割
     */
    @FormUrlEncoded
    @POST("/jokes/jokes/video/list/ids")
    suspend fun getVideoListByIds(
        @Field("ids") ids: String,
    ): JokeListReply?

    /**
     * 获取指定id的段子的点赞列表
     * @param jokeId 段子id
     */
    @FormUrlEncoded
    @POST("/jokes/jokes/video/list")
    suspend fun getTargetJokeLikeList(
        @Field("jokeId") jokeId: Int,
        @Field("page") page: Int,
    ): LikeListReply?

    /**
     * 给段子点赞/取消点赞
     * @param id     段子id
     * @param status true为点赞 false为取消点赞
     */
    @FormUrlEncoded
    @POST("/jokes/jokes/video/list")
    suspend fun likeJoke(
        @Field("id") id: Int,
        @Field("status") status: Boolean,
    ): BaseReply<Any>?
}