package com.fphoenixcorneae.happyjoke.mvi.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

/**
 * @desc：
 * @date：2023/05/10 11:18
 */
@Keep
data class JokeDetailsReply(
    val code: Int = 0, // 200
    val `data`: Data? = null,
    val msg: String? = null // 数据返回成功
) {
    @Keep
    data class Data(
        val info: Info? = null,
        val joke: Joke? = null,
        val user: User? = null
    ) {
        @Keep
        data class Info(
            val commentNum: Int = 0, // 0
            val disLikeNum: Int = 0, // 0
            val isAttention: Boolean = false, // false
            val isLike: Boolean = false, // true
            val isUnlike: Boolean = false, // false
            val likeNum: Int = 0, // 327
            val shareNum: Int = 0 // 0
        )

        @Keep
        data class Joke(
            val addTime: String? = null, // 2019-08-24 13:01:41
            @SerializedName("audit_msg") val auditMsg: Any? = null, // null
            val content: String? = null, // 《在死亡的边缘试探（下集）》
            val hot: Boolean = false, // false
            val imageSize: String? = null, // 330x237
            val imageUrl: String? = null, // ftp://ZQb79GrjmFK1UI4BoacGKeLcxFIp7WGXlVyKPxD8hQUYTrdXQx31WNsMNGGjBZr/OtVzcjNlAS6XXoYApgOegMmZ7rdxQ42HJ8fFlMAOYvZSvpOPFLL1cWNFbfuD8+AuRsUsbhBLSEKyMjEtQ08gcfPkfxe6JP86shzWvasHek9P5DU1MpL35F5TM4q3pPggY6rcfERLabvvA8SpFl94qrkNMkVPaXbwkc8yAHkm8Qed26OkbpIbKDcAOkI03VY+M/eHKJuW1BZsmFazOBfq1NgxQiawqdoasW8ziLg2t4Y=
            val jokesId: Int = 0, // 274903
            val latitudeLongitude: String? = null,
            val showAddress: String? = null,
            val thumbUrl: String? = null,
            val type: Int = 0, // 2
            val userId: Int = 0, // 10142
            val videoSize: String? = null,
            val videoTime: Int = 0, // 0
            val videoUrl: String? = null
        )

        @Keep
        data class User(
            val avatar: String? = null, // http://jokes-avatar.cretinzp.com/qiniu/jokes/avatar/d16454378a404566949d2c6a5f719df2.jpg
            val nickName: String? = null, // 跟空气撒个娇
            val signature: String? = null, // 我不骂人，因为我动手能力比较强
            val userId: Int = 0 // 10142
        )
    }
}