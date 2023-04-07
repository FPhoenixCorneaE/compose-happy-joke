package com.fphoenixcorneae.happyjoke.ext

import com.fphoenixcorneae.happyjoke.tool.DESTool
import java.net.URLDecoder
import java.net.URLEncoder

/**
 * URL "utf-8"编码
 */
fun String?.encodeURL() = runCatching {
    URLEncoder.encode(this, "UTF-8")
}.onFailure {
    it.printStackTrace()
}.getOrNull()

/**
 * URL "utf-8"解码
 */
fun String?.decodeURL() = runCatching {
    URLDecoder.decode(this, "UTF-8")
}.onFailure {
    it.printStackTrace()
}.getOrNull()

/**
 * DES 解密
 */
fun String?.desDecrypt() = DESTool.decryptText(this)