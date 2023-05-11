package com.fphoenixcorneae.happyjoke.mvi.model


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

/**
 * @desc：
 * @date：2023/05/05 17:50
 */
@Keep
data class JokeLikeListReply(
    val code: Int = 0, // 200
    val `data`: List<Data>? = null,
    val msg: String? = null // 数据返回成功
) {
    @Keep
    data class Data(
        val avatar: String? = null, // http://jokes-avatar.cretinzp.com/qiniu/jokes/avatar/ccad99f7c0fc41b19d703b5a0c88f5c9.jpg
        val nickname: String? = null, // 🤔🤔🙃
        @SerializedName("user_id") val userId: Int = 0 // 12725
    )
}