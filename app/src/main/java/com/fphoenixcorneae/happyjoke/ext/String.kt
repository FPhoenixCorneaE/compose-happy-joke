package com.fphoenixcorneae.happyjoke.ext

import com.fphoenixcorneae.happyjoke.tool.AESTool
import java.net.URLDecoder
import java.net.URLEncoder

/**
 * URL "utf-8"编码
 */
fun String?.urlEncode() = runCatching {
    URLEncoder.encode(this, "UTF-8")
}.onFailure {
    it.printStackTrace()
}.getOrNull()

/**
 * URL "utf-8"解码
 */
fun String?.urlDecode() = runCatching {
    URLDecoder.decode(this, "UTF-8")
}.onFailure {
    it.printStackTrace()
}.getOrNull()

/**
 * AES 解密
 */
fun String?.urlAESDecrypt(): String? = this?.run {
    if (startsWith("http://") || !startsWith("ftp://")) {
        this
    } else {
        substring(6)
    }
}?.run {
    AESTool.decrypt(this)
}