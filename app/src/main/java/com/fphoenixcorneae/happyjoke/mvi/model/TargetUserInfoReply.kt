package com.fphoenixcorneae.happyjoke.mvi.model


import androidx.annotation.Keep

/**
 * @desc：
 * @date：2023/05/04 16:20
 */
@Keep
data class TargetUserInfoReply(
    val code: Int = 0, // 200
    val `data`: Data? = null,
    val msg: String? = null // 数据返回成功
) {
    @Keep
    data class Data(
        val attentionNum: String? = null, // 0
        val attentionState: Int = 0, // 0
        val avatar: String? = null, // http://jokes-avatar.cretinzp.com/qiniu/jokes/avatar/429edce78f05427b873ee658096e2f1d.jpg
        val collectNum: String? = null, // 0
        val commentNum: String? = null, // 0
        val cover: String? = null, // http://jokes-avatar.cretinzp.com/qiniu/jokes/avatar/429edce78f05427b873ee658096e2f1d.jpg
        val fansNum: String? = null, // 0
        val joinTime: String? = null, // 3年1个月
        val jokeLikeNum: String? = null, // 948
        val jokesNum: String? = null, // 23
        val likeNum: String? = null, // 1.02k
        val nickname: String? = null, // 非以其无私耶
        val sigbature: String? = null, // 他正在想一个爆炸的签名...
        val userId: Int = 0 // 12985
    )
}