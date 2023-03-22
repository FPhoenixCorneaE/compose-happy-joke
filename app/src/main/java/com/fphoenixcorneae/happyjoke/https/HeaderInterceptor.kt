package com.fphoenixcorneae.happyjoke.https

import com.fphoenixcorneae.happyjoke.const.Constant
import okhttp3.Interceptor
import okhttp3.Response

/**
 * @desc：
 * @date：2023/03/22 10:39
 */
class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        builder
            .addHeader("Content-Type", "application/json")
            .addHeader("project_token", Constant.PROJECT_TOKEN)
            .addHeader("channel", "cretin_open_api")
            .addHeader("token", "")
            .addHeader("uk", "1")
            .addHeader("app", "1.0.0")
            .addHeader("device", "HUAWEI;CDY-AN00")
            .build()
        return chain.proceed(builder.build())
    }
}