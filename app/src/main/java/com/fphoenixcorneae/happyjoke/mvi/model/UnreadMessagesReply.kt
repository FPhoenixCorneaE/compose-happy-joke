package com.fphoenixcorneae.happyjoke.mvi.model


import androidx.annotation.Keep

/**
 * @desc：
 * @date：2023/05/04 10:49
 */
@Keep
data class UnreadMessagesReply(
    val code: Int = 0, // 200
    val `data`: Data? = null,
    val msg: String? = null // 数据返回成功
) {
    @Keep
    data class Data(
        val attentionMum: Int = 0, // 0
        val commentNum: Int = 0, // 0
        val likeNum: Int = 0, // 0
        val systemLatestContent: String? = null, // 恭喜您注册成功，希望您在这里能找到快乐，也能给大家带来快乐~
        val systemNum: Int = 0 // 0
    )
}