package com.fphoenixcorneae.happyjoke.tool

import com.fphoenixcorneae.happyjoke.const.Constant
import com.fphoenixcorneae.happyjoke.ext.clearDS
import com.fphoenixcorneae.happyjoke.ext.getDSFlow
import com.fphoenixcorneae.happyjoke.ext.getSyncDS
import com.fphoenixcorneae.happyjoke.ext.globalScope
import com.fphoenixcorneae.happyjoke.ext.saveDS
import com.fphoenixcorneae.happyjoke.ext.toJson
import com.fphoenixcorneae.happyjoke.ext.toObject
import com.fphoenixcorneae.happyjoke.mvi.model.UserInfoReply
import com.fphoenixcorneae.happyjoke.startup.applicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * @desc：
 * @date：2023/04/24 15:37
 */
object UserManager {

    /**
     * 登录过期
     */
    fun loginExpire() = apply {
        loginState(false)
        saveToken(null)
        saveUserInfo(null)
    }

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

    fun userInfoFlow(): Flow<UserInfoReply.Data?> =
        applicationContext.getDSFlow(Constant.User.USER_INFO, (null as String?))
            .map {
                it.toObject(UserInfoReply.Data::class.java)
            }

    fun logout() {
        globalScope.launch {
            applicationContext.clearDS()
        }
    }
}