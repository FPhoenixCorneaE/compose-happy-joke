package com.fphoenixcorneae.happyjoke.https

import com.fphoenixcorneae.happyjoke.mvi.model.HomepageRecommend
import retrofit2.http.POST

// 双重校验锁式-单例 封装 ApiService 方便直接快速调用简单的接口
val apiService by lazy {
    RetrofitFactory.Builder().build().create(ApiService::class.java)
}

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
}