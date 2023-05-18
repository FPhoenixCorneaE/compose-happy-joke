package com.fphoenixcorneae.happyjoke.https

import com.fphoenixcorneae.happyjoke.https.service.*

/** 双重校验锁式-单例 封装 ApiService 方便直接快速调用简单的接口 */
val homepageService: HomepageService by lazy {
    RetrofitFactory.Builder().build().create(HomepageService::class.java)
}

val userService: UserService by lazy {
    RetrofitFactory.Builder().build().create(UserService::class.java)
}

val messageService: MessageService by lazy {
    RetrofitFactory.Builder().build().create(MessageService::class.java)
}

val jokeService: JokeService by lazy {
    RetrofitFactory.Builder().build().create(JokeService::class.java)
}

val helperService: HelperService by lazy {
    RetrofitFactory.Builder().build().create(HelperService::class.java)
}