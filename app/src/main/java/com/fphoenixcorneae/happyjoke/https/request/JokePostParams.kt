package com.fphoenixcorneae.happyjoke.https.request

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import github.leavesczy.matisse.MediaResource

/**
 * @desc：
 * @date：2023/05/17 11:42
 */
@Keep
data class JokePostParams(
    val content: String, // 内容
    @SerializedName("image_size")
    val imageSize: String? = "", // 图片尺寸，例如1080x2400，如果是多图，则需要拼接所有的图片尺寸，中间用,分割，比如 480x800,1080x900
    @SerializedName("image_urls")
    val imageUrls: String? = "", // 如果type为2 此值代表图片列表，中间用,分割
    val type: String, // 发布类型 1 文本 2 图片 3 视频
    @SerializedName("video_duration")
    val videoDuration: String? = "", // 视频播放时长
    @SerializedName("video_size")
    val videoSize: String? = "", // 视频尺寸 1080x720
    @SerializedName("video_thumbnail_url")
    val videoThumbnailUrl: String? = "", // 视频封面
    @SerializedName("video_url")
    val videoUrl: String? = "", // 视频url，先上传到七牛云之后拿到key
    @Expose(serialize = false, deserialize = false)
    val result: List<MediaResource> = mutableListOf(),
)
