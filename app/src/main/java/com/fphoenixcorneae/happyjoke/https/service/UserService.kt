package com.fphoenixcorneae.happyjoke.https.service

import com.fphoenixcorneae.happyjoke.mvi.model.*
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * @desc：接口地址：http://tools.cretinzp.com/jokes/doc.html
 * @date：2023/04/25 13:52
 */
interface UserService {
    /**
     * 获取登录验证码
     * @param phone 手机号
     */
    @FormUrlEncoded
    @POST("/jokes/user/login/get_code")
    suspend fun getCode(
        @Field("phone") phone: String,
    ): BaseReply<Any>?

    /**
     * 验证码登录
     * @param phone 手机号
     * @param code  验证码
     */
    @FormUrlEncoded
    @POST("/jokes/user/login/code")
    suspend fun loginByCode(
        @Field("phone") phone: String,
        @Field("code") code: String,
    ): LoginReply?

    /**
     * 账号密码登录
     * @param phone 手机号
     * @param psw   密码 无需加密，后台加密
     */
    @FormUrlEncoded
    @POST("/jokes/user/login/psw")
    suspend fun loginByPsw(
        @Field("phone") phone: String,
        @Field("psw") psw: String,
    ): LoginReply?

    /**
     * 重置密码获取验证码
     * @param phone    手机号
     */
    @FormUrlEncoded
    @POST("/jokes/user/psw/reset/get_code")
    suspend fun passwordResetGetCode(
        @Field("phone") phone: String,
    ): BaseReply<Any>?

    /**
     * 重置密码
     * @param phone    手机号
     * @param code     验证码
     * @param password 密码 无需加密，后台加密
     */
    @FormUrlEncoded
    @POST("/jokes/user/psw/reset")
    suspend fun passwordReset(
        @Field("phone") phone: String,
        @Field("code") code: String,
        @Field("password") password: String,
    ): BaseReply<Any>?

    /**
     * 获取用户信息
     */
    @POST("/jokes/user/info")
    suspend fun getUserInfo(): UserInfoReply?
}