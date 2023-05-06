package com.fphoenixcorneae.happyjoke.mvi.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

/**
 * @desc：
 * @date：2023/03/22 17:21
 */
@Keep
data class JokeListReply(
    val code: Int = 0, // 200
    val `data`: List<Data>? = null,
    val msg: String? = null // 数据返回成功
) {
    @Keep
    data class Data(
        val info: Info? = null, // 段子社交信息
        val joke: Joke? = null, // 段子内容
        val user: User? = null, // 所属用户
    ) {
        @Keep
        data class Info(
            val commentNum: Int = 0, // 评论数
            val disLikeNum: Int = 0, // 踩的数量
            val isAttention: Boolean = false, // 是否关注
            val isLike: Boolean = false, // 是否喜欢
            val isUnlike: Boolean = false, // 是否踩
            val likeNum: Int = 0, // 点赞数
            val shareNum: Int = 0 // 分享数
        )

        @Keep
        data class Joke(
            val addTime: String? = null, // 添加时间，已经格式化完成: 2018-04-24 15:13:25
            @SerializedName("audit_msg") val auditMsg: String? = null, // 审核失败信息
            val content: String? = null, // 段子内容: 老妈年已五旬，经常担心智力退化、老年痴呆，每次上街买菜，一样一样的菜价全部心算合计，居然和商贩的计算器算出来的总价差不多，心里那个美啊，咱智力优秀，没问题。一边走，一边美，经常听到商贩在身后喊“哎，那位大姐，您的菜忘拿了”。
            val hot: Boolean = false, // 是否是热门
            val imageSize: String? = null, // 图片尺寸信息
            val imageUrl: String? = null, // type=2 此值有数据，多图用,分割，最多9张图: ftp://iQTEsFjrrkopN4gOR3lZ9/TMpWjPFyr8UQjmuxBztPo=
            val jokesId: Int = 0, // 段子id
            val latitudeLongitude: String? = null, // 发布时候经纬度信息
            val showAddress: String? = null, // 发布时候的位置信息
            val thumbUrl: String? = null, // 视频封面
            val type: Int = 0, // 段子类型 1 文本 2 图片 >=3 视频
            val userId: Int = 0, // 段子所属用户id
            val videoSize: String? = null, // 视频尺寸: 960,540
            val videoTime: Int = 0, // 视频时长
            val videoUrl: String? = null, // 视频地址
        )

        @Keep
        data class User(
            val avatar: String? = null, // 用户头像: http://jokes-avatar.cretinzp.com/qiniu/jokes/avatar/default/default_prod_11301.jpg
            val nickName: String? = null, // 用户昵称: 採姑娘的小魔菇
            val signature: String? = null, // 用户签名信息: 天王盖地虎 小鸡炖蘑菇……
            val userId: Int = 0 // 用户id
        )
    }
}