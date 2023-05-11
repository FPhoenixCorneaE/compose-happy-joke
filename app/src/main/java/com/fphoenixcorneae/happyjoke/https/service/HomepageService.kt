package com.fphoenixcorneae.happyjoke.https.service

import com.fphoenixcorneae.happyjoke.mvi.model.*
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * @desc：接口地址：http://tools.cretinzp.com/jokes/doc.html
 * @date：2023/03/22 10:05
 */
interface HomepageService {

    /**
     * 获取首页的推荐列表数据
     */
    @POST("/jokes/home/recommend")
    suspend fun homepageRecommend(): JokeListReply?

    /**
     * 获取首页的推荐关注数据
     */
    @POST("/jokes/home/attention/recommend")
    suspend fun homepageAttentionRecommend(): AttentionRecommendReply?

    /**
     * 获取关注的用户发布的段子列表
     */
    @FormUrlEncoded
    @POST("/jokes/home/attention/list")
    suspend fun homepageAttentionList(@Field("page") page: Int): JokeListReply?

    /**
     * 获取首页的最新列表数据
     */
    @POST("/jokes/home/latest")
    suspend fun homepageLatest(): JokeListReply?

    /**
     * 获取首页的纯文列表数据
     */
    @POST("/jokes/home/text")
    suspend fun homepagePoliteLetters(): JokeListReply?

    /**
     * 获取首页的纯图片列表数据
     */
    @POST("/jokes/home/pic")
    suspend fun homepageFunnyPictures(): JokeListReply?

    /**
     * 获取划一划页面的推荐列表数据
     */
    @POST("/jokes/douyin/list")
    suspend fun sweepTikTokVideos(): JokeListReply?
}