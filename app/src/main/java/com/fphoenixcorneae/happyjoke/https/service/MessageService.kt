package com.fphoenixcorneae.happyjoke.https.service

import com.fphoenixcorneae.happyjoke.mvi.model.UnreadMessagesReply
import retrofit2.http.POST

/**
 * @desc：接口地址：http://tools.cretinzp.com/jokes/doc.html
 * @date：2023/05/04 10:48
 */
interface MessageService {

    /**
     * 获取当前用户的未读消息数
     */
    @POST("/jokes/user/message/unread")
    suspend fun getUnreadMessages(): UnreadMessagesReply?
}