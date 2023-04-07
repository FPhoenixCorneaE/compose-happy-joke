package com.fphoenixcorneae.happyjoke.tool

import android.util.Base64
import android.util.Log
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESKeySpec

/**
 * @desc：
 * @date：2023/04/07 17:27
 */
object DESTool {

    private const val KEY = "cretin**273846"

    private fun createCipher() = Cipher.getInstance("DES")

    private fun getSecretKey() = SecretKeyFactory.getInstance("DES")
        .generateSecret(DESKeySpec(KEY.toByteArray()))

    private fun ByteArray?.toBase64ByteArray() = run {
        if (this == null || isEmpty()) {
            ByteArray(0)
        } else {
            Base64.decode(this, Base64.NO_WRAP)
        }
    }

    fun decryptText(s: String?) = runCatching {
        Log.e("DESTool", "解密原字符串：$s")
        val c = createCipher()
        c.init(Cipher.DECRYPT_MODE, getSecretKey())
        c.doFinal(s?.toByteArray().toBase64ByteArray()).toString()
    }.onFailure {
        Log.e("DESTool", "解密异常：$it")
        it.printStackTrace()
    }.getOrNull()
}