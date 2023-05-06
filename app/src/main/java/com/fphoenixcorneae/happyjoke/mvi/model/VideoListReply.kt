package com.fphoenixcorneae.happyjoke.mvi.model


import androidx.annotation.Keep

/**
 * @desc：
 * @date：2023/05/05 17:25
 */
@Keep
data class VideoListReply(
    val code: Int = 0, // 200
    val `data`: List<Data>? = null,
    val msg: String? = null // 数据返回成功
) {
    @Keep
    data class Data(
        val cover: String? = null, // ftp://2oxS7wXkFk0oAx/nG7NCahnOx2bOqQgWml1B22Zv5xCfFIimU6P0WMKSjJZ54kH9oUTSfNKOscI+8m1igXu8Lw==
        val jokeId: Int = 0, // 338823
        val likeNum: String? = null // 0
    )
}