package com.fphoenixcorneae.happyjoke.mvi.model

import androidx.annotation.Keep

/**
 * @desc：
 * @date：2023/04/23 16:07
 */
@Keep
data class LoginReply(
    val code: Int = 0, // 0
    val `data`: Data? = null,
    val msg: String? = null
) {
    @Keep
    data class Data(
        val token: String? = null,
        val type: String? = null,
        val userInfo: UserInfo? = null
    ) {
        @Keep
        data class UserInfo(
            val avatar: String? = null,
            val birthday: String? = null,
            val inviteCode: String? = null,
            val invitedCode: String? = null,
            val nickname: String? = null,
            val sex: String? = null,
            val signature: String? = null,
            val userId: Int = 0, // 0
            val userPhone: String? = null
        )
    }
}