package com.fphoenixcorneae.happyjoke.tool

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
    private const val TRANSFORMATION = "AES/ECB/PKCS5Padding"
    private const val ALGORITHM = "AES"

    private fun createCipher(): Cipher = Cipher.getInstance(/* transformation = */ TRANSFORMATION)

    private fun getSecretKey(): SecretKey = SecretKeySpec(/* key = */ KEY.toByteArray(), /* algorithm = */ ALGORITHM)

    private fun ByteArray?.toBase64ByteArray(): ByteArray = run {
        if (this == null || isEmpty()) {
            byteArrayOf()
        } else {
            Base64.decode(this, Base64.DEFAULT)
        }
    }

    /**
     * 加密后再进行Base64编码
     */
    fun encrypt(s: String?): String? = runCatching {
        val c = createCipher()
        c.init(Cipher.ENCRYPT_MODE, getSecretKey())
        String(Base64.encode(c.doFinal(s?.toByteArray()), Base64.DEFAULT))
    }.onFailure {
        it.printStackTrace()
    }.getOrNull()

    /**
     * 先进行Base64解码再解密
     */
    fun decrypt(s: String?): String? = runCatching {
        val c = createCipher()
        c.init(Cipher.DECRYPT_MODE, getSecretKey())
        String(c.doFinal(Base64.decode(s?.toByteArray(), Base64.DEFAULT)))
    }.onFailure {
        it.printStackTrace()
    }.getOrNull()
}