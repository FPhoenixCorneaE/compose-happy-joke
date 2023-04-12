package com.fphoenixcorneae.happyjoke.tool

import android.annotation.SuppressLint
import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

/**
 * @desc：AES加密/解密
 * @date：2023/04/07 17:27
 */
object AESTool {

    private const val KEY = "cretinzp**273846"

    /** "算法/模式/补码方式" */
    private const val TRANSFORMATION = "AES/ECB/PKCS5Padding"
    private const val ALGORITHM = "AES"

    @SuppressLint("GetInstance")
    private fun createCipher(): Cipher = Cipher.getInstance(/* transformation = */ TRANSFORMATION)

    private fun getSecretKey(): SecretKey = SecretKeySpec(/* key = */ KEY.toByteArray(), /* algorithm = */ ALGORITHM)

    /**
     * 加密后再进行Base64编码
     */
    fun encrypt(s: String?): String? = runCatching {
        val c = createCipher()
        c.init(Cipher.ENCRYPT_MODE, getSecretKey())
        val encrypted = c.doFinal(s?.toByteArray())
        // 此处使用Base64做转码功能，能起到二次加密的作用
        Base64.encodeToString(encrypted, Base64.NO_WRAP)
    }.onFailure {
        it.printStackTrace()
    }.getOrNull()

    /**
     * 先进行Base64解码再解密
     */
    fun decrypt(s: String?): String? = runCatching {
        val c = createCipher()
        c.init(Cipher.DECRYPT_MODE, getSecretKey())
        // 先Base64解码
        val encrypted = Base64.decode(s?.toByteArray(), Base64.NO_WRAP)
        val original = c.doFinal(encrypted)
        String(original)
    }.onFailure {
        it.printStackTrace()
    }.getOrNull()
}