package com.fphoenixcorneae.happyjoke.https.service

import com.fphoenixcorneae.happyjoke.mvi.model.QiNiuTokenReply
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * @desc：接口地址：http://tools.cretinzp.com/jokes/doc.html
 * @date：2023/05/18 11:12
 */
interface HelperService {

    /**
     * 获取七牛云token
     * @param type     类型: 0 获取普通token   1 获取头像token
     * @param filename 文件名称，要带后缀
     */
    @FormUrlEncoded
    @POST("/jokes/helper/qiniu/token")
    suspend fun getQiNiuToken(
        @Field("type") type: Int,
        @Field("filename") filename: String,
    ): QiNiuTokenReply?
}