package com.fphoenixcorneae.happyjoke.https

import retrofit2.http.POST

val apiService by lazy {
    RetrofitFactory.Builder().build().create(ApiService::class.java)
}

/**
 * @desc：
 * @date：2023/03/22 10:05
 */
interface ApiService {

    /**
     * 获取主页的推荐列表数据
     */
    @POST("/jokes/home/recommend")
    suspend fun homeRecommend(): Any?
}