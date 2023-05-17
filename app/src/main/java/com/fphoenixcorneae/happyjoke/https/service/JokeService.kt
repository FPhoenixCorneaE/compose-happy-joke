package com.fphoenixcorneae.happyjoke.https.service

import com.fphoenixcorneae.happyjoke.mvi.model.*
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * @desc：接口地址：http://tools.cretinzp.com/jokes/doc.html
 * @date：2023/05/05 17:13
 */
interface JokeService {
    /**
     * 发布段子
     * 使用 @JvmSuppressWildcards 避免出现异常：
     * lang.IllegalArgumentException:
     * Parameter type must not include a type variable or wildcard: java.util.Map<java.lang.String, ?>
     */
    @FormUrlEncoded
    @POST("/jokes/jokes/post")
    @JvmSuppressWildcards
    suspend fun postJoke(
        @FieldMap params: Map<String, Any?>?,
    ): BaseReply<Any>?

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
     * 获取指定id的段子
     * @param jokeId 段子id
     */
    @FormUrlEncoded
    @POST("/jokes/jokes/target")
    suspend fun getJokeById(
        @Field("jokeId") jokeId: String,
    ): JokeDetailsReply?

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
     * 获取评论列表
     * @param jokeId 段子id
     * @param page   分页
     */
    @FormUrlEncoded
    @POST("/jokes/jokes/comment/list")
    suspend fun getTargetJokeCommentList(
        @Field("jokeId") jokeId: String,
        @Field("page") page: Int,
    ): JokeCommentListReply?

    /**
     * 获取指定id的段子的点赞列表
     * @param jokeId 段子id
     */
    @FormUrlEncoded
    @POST("/jokes/jokes/like/list")
    suspend fun getTargetJokeLikeList(
        @Field("jokeId") jokeId: String,
        @Field("page") page: Int,
    ): JokeLikeListReply?

    /**
     * 给段子点赞/取消点赞
     * @param id     段子id
     * @param status true为点赞 false为取消点赞
     */
    @FormUrlEncoded
    @POST("/jokes/jokes/like")
    suspend fun likeJoke(
        @Field("id") id: String,
        @Field("status") status: Boolean,
    ): BaseReply<Any>?

    /**
     * 获取审核列表
     * @param status 状态 0 审核中 1 审核失败
     */
    @FormUrlEncoded
    @POST("/jokes/jokes/audit/list")
    suspend fun getAuditList(
        @Field("status") status: String,
        @Field("page") page: Int,
    ): JokeListReply?
}