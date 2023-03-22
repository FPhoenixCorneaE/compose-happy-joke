package com.fphoenixcorneae.happyjoke.https

import com.fphoenixcorneae.happyjoke.const.Constant
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @desc：
 * @date：2023/03/22 10:06
 */
class RetrofitFactory {
    class Builder {

        fun build(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(Constant.HOST_URL)
                .client(buildOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        private fun buildOkHttpClient(): OkHttpClient {
            return OkHttpClient.Builder()
                // 添加请求header, 注意要设置在日志拦截器之前, 不然Log中会不显示header信息
                .addInterceptor(HeaderInterceptor())
                // 日志拦截器
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .sslSocketFactory(SSLSocketClient.getSocketFactory(), SSLSocketClient.x509TrustManager)
                .hostnameVerifier(SSLSocketClient.hostnameVerifier)
                .build()
        }
    }
}