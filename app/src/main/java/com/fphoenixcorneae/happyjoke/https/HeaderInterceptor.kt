package com.fphoenixcorneae.happyjoke.https

import com.fphoenixcorneae.happyjoke.const.Constant
import com.fphoenixcorneae.happyjoke.ext.*
import com.fphoenixcorneae.happyjoke.tool.UserManager
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
            // 用户凭证信息，登录成功之后接口会返回token，你需要存储在本地，每次请求带上
            .addHeader("token", UserManager.getToken())
            // 用户设备唯一标识，设备的唯一id，请尽量保证针对设备唯一，长度32以内
            .addHeader("uk", "$appUid")
            // app信息，拼接版本号版本标识和系统版本，用;分开，例如 1.0.0;1;10，代表版本号1.0.0，版本标识1，系统为Android 10，其他也类似
            .addHeader("app", "$appVersionName;$appVersionCode;10")
            // 设备信息，拼接版本设备信息，设备品牌和设备型号，用;分开，例如HUAWEI;CDY-AN00
            .addHeader("device", "$deviceManufacturer;$deviceModel")
            .build()
        return chain.proceed(builder.build())
    }
}