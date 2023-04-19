package com.fphoenixcorneae.happyjoke.mvi.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

/**
 * @desc：
 * @date：2023/04/14 17:20
 */
@Keep
data class SweepTikTokVideo(
    val code: Int = 0, // 200
    val `data`: List<Data>? = null,
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
            val isLike: Boolean = false, // false
            val isUnlike: Boolean = false, // false
            val likeNum: Int = 0, // 0
            val shareNum: Int = 0 // 0
        )

        @Keep
        data class Joke(
            val addTime: String? = null, // 2019-09-12 00:04:01
            @SerializedName("audit_msg") val auditMsg: String? = null,
            val content: String? = null, // 给一些年轻人读抑郁症患者的微博，他们起初是嘲笑的，后来知道这些发微博的人已经离开了这个世界，他们都沉默了……抑郁症患者的微博那些“矫情”的话，可能是抑郁症患者最后的“求救”。如果他们没能爱上这个世界，让他们知道还有你爱TA。
            val hot: Boolean = false, // false
            val imageSize: String? = null,
            val imageUrl: String? = null,
            val jokesId: Int = 0, // 138134
            val latitudeLongitude: String? = null,
            val showAddress: String? = null,
            val thumbUrl: String? = null, // ftp://ZQb79GrjmFK1UI4BoacGKeLcxFIp7WGXlVyKPxD8hQUYTrdXQx31WNsMNGGjBZr/sIXVZec/37EnJeg8blOi7qUTkZfYtu5ShQ2yNQFlfVpKsBX3MJqNPJ8sYg40Znu27Z9FcPxTn6n4nBfcolrcJGkT0dGhgSv2j/LSgH+vRsmW4Sl0I+dkFAbXEnyVVYzkq/YKWsd7k4BrbucGtWBV3BbPDnE0c1KBP8HPk/ujwM+PgVzl6MOfV9qW2IQbJ3ubzBe9w221fF4IgixhdBy21r0FzJc5CCT/9kUnP/L5WE5sA2bhv4l2P6Y+B1iGd9yl
            val type: Int = 0, // 3
            val userId: Int = 0, // 10580
            val videoSize: String? = null, // 640,368
            val videoTime: Int = 0, // 244700000
            val videoUrl: String? = null // ftp://ZQb79GrjmFK1UI4BoacGKeLcxFIp7WGXlVyKPxD8hQUYTrdXQx31WNsMNGGjBZr/foibU1cuvRArycfN/+/fRxZlUwQRJIrvUvIdpXoM2fBhtgZgTFGu6B4kVN0zIrN683u2vcxQWhTYtOIiEz4VPvxdnhqT5NPJv0631KnM/u2LTJzfm2xA2psfmEnv29bFY6rcfERLabvvA8SpFl94qrkNMkVPaXbwkc8yAHkm8QfoQmCdVlfO2OG+ysHR6x3wVnlfSVZkosfKbxfDr/Qkl//B2zVBTpyKTIvLgJB8BsM=
        )

        @Keep
        data class User(
            val avatar: String? = null, // http://jokes-avatar.cretinzp.com/qiniu/jokes/avatar/36b9b413d6ef45f193d3c292ad1cf08b.jpg
            val nickName: String? = null, // 旧梦如风°
            val signature: String? = null, // 同志，您辛苦了，请继续！
            val userId: Int = 0 // 10580
        )
    }
}