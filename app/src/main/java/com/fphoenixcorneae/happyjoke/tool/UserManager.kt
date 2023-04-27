package com.fphoenixcorneae.happyjoke.tool

import com.fphoenixcorneae.happyjoke.const.Constant
import com.fphoenixcorneae.happyjoke.ext.*
import com.fphoenixcorneae.happyjoke.mvi.model.UserInfoReply
import com.fphoenixcorneae.happyjoke.startup.applicationContext
import kotlinx.coroutines.launch

/**
 * @desc：
 * @date：2023/04/24 15:37
 */
object UserManager {

    fun loginState(success: Boolean) = apply {
        globalScope.launch {
            applicationContext.saveDS(Constant.User.IS_LOGGED_IN, success)
        }
    }

    fun isLoggedIn() = applicationContext.getSyncDS(Constant.User.IS_LOGGED_IN, false)

    fun loginStateFlow() = applicationContext.getDSFlow(Constant.User.IS_LOGGED_IN, false)

    fun saveToken(token: String?) = apply {
        globalScope.launch {
            applicationContext.saveDS(Constant.User.TOKEN, token)
        }
    }

    fun getToken() = applicationContext.getSyncDS(Constant.User.TOKEN, "")

    fun saveUserInfo(userInfo: UserInfoReply.Data?) = apply {
        globalScope.launch {
            applicationContext.saveDS(Constant.User.USER_INFO, userInfo.toJson())
        }
    }

    fun getUserInfo() = applicationContext.getSyncDS(Constant.User.USER_INFO, "")
        .toObject(UserInfoReply.Data::class.java)

    fun logout() {
        globalScope.launch {
            applicationContext.clearDS()
        }
    }
}