package com.fphoenixcorneae.happyjoke.mvi.model


import androidx.annotation.Keep

/**
 * @desc：
 * @date：2023/04/27 17:18
 */
@Keep
data class UserAttentionReply(
    val code: Int = 0, // 200
    val `data`: Data? = null,
    val msg: String? = null // 数据返回成功
) {
    @Keep
    data class Data(
        val attentionState: Int = 0 // 2
    )
}