package com.fphoenixcorneae.happyjoke.tool

/**
 * @desc：
 * @date：2023/05/18 16:28
 */
object QiNiuHelper {

    private const val BUCKET_VIDEO = "qiniu/jokes/video/"
    private const val BUCKET_IMAGE = "qiniu/jokes/image/"
    private const val BUCKET_AVATAR = "qiniu/jokes/avatar/"

    fun getBucket(type: Int = 0) = run {
        when (type) {
            1 -> BUCKET_AVATAR
            2 -> BUCKET_VIDEO
            else -> BUCKET_IMAGE
        }
    }
}