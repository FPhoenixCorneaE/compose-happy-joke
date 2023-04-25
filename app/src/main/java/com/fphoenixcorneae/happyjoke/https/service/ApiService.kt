package com.fphoenixcorneae.happyjoke.https.service

import com.fphoenixcorneae.happyjoke.mvi.model.*
import retrofit2.http.POST

/**
 * @desc：接口地址：http://tools.cretinzp.com/jokes/doc.html
 * @date：2023/03/22 10:05
 */
interface ApiService {

    /**
     * 获取首页的推荐列表数据
     */
    @POST("/jokes/home/recommend")
    suspend fun homepageRecommend(): HomepageRecommend?

    /**
     * 获取首页的推荐关注数据
     */
    @POST("/jokes/home/attention/recommend")
    suspend fun homepageAttentionRecommend(): AttentionRecommend?

    /**
     * 获取首页的最新列表数据
     */
    @POST("/jokes/home/latest")
    suspend fun homepageLatest(): HomepageRecommend?

    /**
     * 获取首页的纯文列表数据
     */
    @POST("/jokes/home/text")
    suspend fun homepagePoliteLetters(): HomepageRecommend?

    /**
     * 获取首页的纯图片列表数据
     */
    @POST("/jokes/home/pic")
    suspend fun homepageFunnyPictures(): HomepageRecommend?

    /**
     * 获取划一划页面的推荐列表数据
     */
    @POST("/jokes/douyin/list")
    suspend fun sweepTikTokVideo(): SweepTikTokVideo?
}