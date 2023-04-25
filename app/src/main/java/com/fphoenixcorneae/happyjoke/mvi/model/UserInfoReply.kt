package com.fphoenixcorneae.happyjoke.mvi.model


import androidx.annotation.Keep

@Keep
data class UserInfoReply(
    val code: Int = 0, // 200
    val `data`: Data? = null,
    val msg: String? = null // 数据返回成功
) {
    @Keep
    data class Data(
        val info: Info? = null,
        val user: User? = null
    ) {
        @Keep
        data class Info(
            val attentionNum: Int = 0, // 0
            val experienceNum: Int = 0, // 0
            val fansNum: Int = 0, // 0
            val likeNum: Int = 0 // 0
        )

        @Keep
        data class User(
            val avatar: String? = null, // https://jokes-avatar.oss-cn-beijing.aliyuncs.com/aliyun/jokes/avatar/default_avatar.png
            val birthday: String? = null,
            val inviteCode: String? = null, // XUK4IX
            val invitedCode: Any? = null, // null
            val nickname: String? = null, // 乐-XUK4IX
            val sex: String? = null, // 男
            val signature: String? = null, // 他正在想一个爆炸的签名...
            val userId: Int = 0, // 15023
            val userPhone: String? = null // 131****0000
        )
    }
}