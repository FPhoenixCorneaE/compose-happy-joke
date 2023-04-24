package com.fphoenixcorneae.happyjoke.mvi.model

import androidx.annotation.Keep

/**
 * @desc：
 * @date：2023/04/23 11:43
 */
@Keep
data class BaseReply<T>(
    val code: Int = 0, // 0
    val `data`: T? = null,
    val msg: String? = null,
) {
    companion object {
        const val OK = 200
    }
}