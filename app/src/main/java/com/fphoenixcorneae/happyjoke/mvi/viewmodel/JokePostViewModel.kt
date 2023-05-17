package com.fphoenixcorneae.happyjoke.mvi.viewmodel

import com.fphoenixcorneae.happyjoke.ext.launchDefault
import com.fphoenixcorneae.happyjoke.ext.launchIo
import com.fphoenixcorneae.happyjoke.ext.toJson
import com.fphoenixcorneae.happyjoke.ext.toObject
import com.fphoenixcorneae.happyjoke.https.doOnError
import com.fphoenixcorneae.happyjoke.https.doOnSuccess
import com.fphoenixcorneae.happyjoke.https.httpRequest
import com.fphoenixcorneae.happyjoke.https.jokeService
import com.fphoenixcorneae.happyjoke.https.request.JokePostParams
import com.fphoenixcorneae.happyjoke.mvi.model.BaseReply
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

/**
 * @desc：
 * @date：2023/05/17 14:00
 */
class JokePostViewModel : BaseViewModel<JokePostAction>() {

    private val _jokePostUiState = MutableStateFlow(JokePostUiState())
    val jokePostUiState = _jokePostUiState.asStateFlow()

    fun contentChanged(content: String) {
        launchDefault {
            _jokePostUiState.update {
                it.copy(jokePostParams = it.jokePostParams.copy(content = content))
            }
        }
    }

    override fun dealIntent(action: JokePostAction) {
        when (action) {
            JokePostAction.PostJoke -> launchIo {
                val params = withContext(Dispatchers.Default) {
                    _jokePostUiState.first().jokePostParams.toJson()
                        .toObject<Map<String, Any?>>(object : TypeToken<Map<String, Any?>>() {}.type)
                }
                httpRequest {
                    jokeService.postJoke(params = params)
                }.doOnSuccess { reply ->
                    _jokePostUiState.update {
                        it.copy(postResultMsg = reply?.msg, postSuccess = reply?.code == BaseReply.OK)
                    }
                }.doOnError { t ->
                    _jokePostUiState.update {
                        it.copy(postResultMsg = t.message, postSuccess = false)
                    }
                }
            }
        }
    }
}

/**
 * @desc：
 * @date：2023/05/17 14:01
 */
data class JokePostUiState(
    val jokePostParams: JokePostParams = JokePostParams(content = "", type = "1"),
    val postResultMsg: String? = null,
    val postSuccess: Boolean = false,
) {
    fun isContentBlank() = jokePostParams.content.isBlank()

    fun isContentExceedLimit() = jokePostParams.content.length > 300
}

/**
 * @desc：
 * @date：2023/05/17 14:00
 */
sealed class JokePostAction {
    /** 发布帖子 */
    object PostJoke : JokePostAction()
}