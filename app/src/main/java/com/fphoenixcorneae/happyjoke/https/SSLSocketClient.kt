package com.fphoenixcorneae.happyjoke.https

import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.*

/**
 * @desc：
 * @date：2023/03/22 13:46
 */
object SSLSocketClient {

    @Throws(NoSuchAlgorithmException::class, KeyManagementException::class)
    fun getSocketFactory(): SSLSocketFactory {
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, arrayOf(x509TrustManager), SecureRandom())
        return sslContext.socketFactory
    }

    val x509TrustManager: X509TrustManager
        get() = object : X509TrustManager {
            @Throws(CertificateException::class)
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            @Throws(CertificateException::class)
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        }

    val hostnameVerifier: HostnameVerifier
        get() = HostnameVerifier { s: String?, sslSession: SSLSession? -> true }
}