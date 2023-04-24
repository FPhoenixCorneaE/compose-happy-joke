package com.fphoenixcorneae.happyjoke.tool

import com.fphoenixcorneae.happyjoke.const.Constant
import com.fphoenixcorneae.happyjoke.ext.*
import com.fphoenixcorneae.happyjoke.mvi.model.LoginReply
import com.fphoenixcorneae.happyjoke.startup.applicationContext
import kotlinx.coroutines.launch

/**
 * @desc：
 * @date：2023/04/24 15:37
 */
object UserManager {

    fun saveToken(token: String?) = apply {
        globalScope.launch {
            applicationContext.saveDS(Constant.User.TOKEN, token)
        }
    }

    fun getToken() = applicationContext.getSyncDS(Constant.User.TOKEN, "")

    fun saveUserInfo(userInfo: LoginReply.Data.UserInfo?) = apply {
        globalScope.launch {
            applicationContext.saveDS(Constant.User.USER_INFO, userInfo.toJson())
        }
    }

    fun getUserInfo() = applicationContext.getSyncDS(Constant.User.USER_INFO, "")
        .toObject(LoginReply.Data.UserInfo::class.java)
}