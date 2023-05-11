package com.fphoenixcorneae.happyjoke.mvi.model


import androidx.annotation.Keep

/**
 * @desc：
 * @date：2023/04/17 13:52
 */
@Keep
data class AttentionRecommendReply(
    val code: Int = 0, // 200
    val `data`: List<Data>? = null,
    val msg: String? = null // 数据返回成功
) {
    @Keep
    data class Data(
        val avatar: String? = null, // http://jokes-avatar.cretinzp.com/qiniu/jokes/avatar/b09dfcb742bb4ef1b0c4cc0f38612a6e.jpg
        val fansNum: String? = null, // 1
        val isAttention: Boolean = false, // false
        val jokesNum: String? = null, // 139
        val nickname: String? = null, // 无与为乐者.
        val userId: Int = 0 // 8747
    )
}