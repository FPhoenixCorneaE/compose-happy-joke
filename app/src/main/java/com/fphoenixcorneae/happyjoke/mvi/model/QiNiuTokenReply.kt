package com.fphoenixcorneae.happyjoke.mvi.model

import androidx.annotation.Keep

/**
 * @desc：
 * @date：2023/05/18 11:17
 */
@Keep
data class QiNiuTokenReply(
    val code: Int = 0, // 200
    val `data`: Data? = null,
    val msg: String? = null, // 数据返回成功
) {
    @Keep
    data class Data(
        val token: String? = null, // dDzvWsb8wd1RAL-icMJHfM_YonGrmtPhIIlIcL1K:EDsP5r9XPNxoCy1JM1X2xe7bXEs=:eyJzY29wZSI6Impva2VzLWF2YXRhcjpvcGVuLXNvdXJjZS9hdmF0YXIvNmM1N2Y4MzZlNWEyNDBlMTlkZWE3NWMwYTMyNzhiM2QucG5nIiwiZGVhZGxpbmUiOjE2ODQzODMzODN9
    )
}