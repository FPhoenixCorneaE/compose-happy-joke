package com.fphoenixcorneae.happyjoke.https

import com.fphoenixcorneae.happyjoke.https.service.ApiService
import com.fphoenixcorneae.happyjoke.https.service.UserService

// 双重校验锁式-单例 封装 ApiService 方便直接快速调用简单的接口
val apiService: ApiService by lazy {
    RetrofitFactory.Builder().build().create(ApiService::class.java)
}

// 双重校验锁式-单例 封装 ApiService 方便直接快速调用简单的接口
val userService: UserService by lazy {
    RetrofitFactory.Builder().build().create(UserService::class.java)
}