package com.fphoenixcorneae.happyjoke.mvi.model


import androidx.annotation.Keep

/**
 * @desc：
 * @date：2023/05/10 15:43
 */
@Keep
data class JokeCommentListReply(
    val code: Int = 0, // 200
    val `data`: Data? = null,
    val msg: String? = null // 数据返回成功
) {
    @Keep
    data class Data(
        val comments: List<Comment>? = null,
        val count: Int = 0 // 1
    ) {
        @Keep
        data class Comment(
            val commentId: Int = 0, // 9618
            val commentUser: CommentUser? = null,
            val content: String? = null, // 😍
            val isLike: Boolean = false, // false
            val itemCommentList: Any? = null, // null
            val itemCommentNum: Int = 0, // 0
            val jokeId: Int = 0, // 338405
            val jokeOwnerUserId: Int = 0, // 0
            val likeNum: Int = 0, // 0
            val timeStr: String? = null // 2022/08/16 09:53
        ) {
            @Keep
            data class CommentUser(
                val nickname: String? = null, // 乐-ZR3TTO
                val userAvatar: String? = null, // http://jokes-avatar.cretinzp.com/qiniu/jokes/avatar/14e1a81f61834ff1a4012dd375cf26bf.jpeg
                val userId: Int = 0 // 14950
            )
        }
    }
}