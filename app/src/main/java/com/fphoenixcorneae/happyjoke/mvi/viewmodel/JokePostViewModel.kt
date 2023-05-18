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
import github.leavesczy.matisse.MediaResource
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

    fun imageUrlsChanged(result: List<MediaResource>) {
        launchDefault {
            _jokePostUiState.update {
                val imageUrls = it.imageUrls()
                val imageSize = it.imageSize()
                result.forEach { mediaResource ->
                    imageUrls.add(mediaResource.uri.toString())
                    imageSize.add("${mediaResource.width}x${mediaResource.height}")
                }
                it.copy(
                    jokePostParams = it.jokePostParams.copy(
                        imageUrls = imageUrls.joinToString(","),
                        imageSize = imageSize.joinToString(","),
                    )
                )
            }
        }
    }

    fun removeMediaResource(index: Int) {
        launchDefault {
            if (
                (_jokePostUiState.first().jokePostParams.type == "2" && _jokePostUiState.first().imageUrls().size == 1)
                || _jokePostUiState.first().jokePostParams.type == "3"
            ) {
                textType()
            } else {
                _jokePostUiState.update {
                    val imageUrls = it.imageUrls()
                    val imageSize = it.imageSize()
                    imageUrls.removeAt(index)
                    imageSize.removeAt(index)
                    it.copy(
                        jokePostParams = it.jokePostParams.copy(
                            imageUrls = imageUrls.joinToString(","),
                            imageSize = imageSize.joinToString(","),
                        )
                    )
                }
            }
        }
    }

    fun textType() {
        launchDefault {
            _jokePostUiState.update {
                it.copy(jokePostParams = JokePostParams(content = it.jokePostParams.content, type = "1"))
            }
        }
    }

    fun imageType() {
        launchDefault {
            _jokePostUiState.update {
                it.copy(
                    jokePostParams = it.jokePostParams.copy(
                        type = "2",
                        videoDuration = "",
                        videoSize = "",
                        videoThumbnailUrl = "",
                        videoUrl = "",
                    )
                )
            }
        }
    }

    fun videoType() {
        launchDefault {
            _jokePostUiState.update {
                it.copy(
                    jokePostParams = it.jokePostParams.copy(
                        type = "3",
                        imageUrls = "",
                        imageSize = "",
                    )
                )
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

    fun imageUrls() = jokePostParams.imageUrls.orEmpty().split(",")
        .filter { it.isNotBlank() }.toMutableList()

    fun imageSize() = jokePostParams.imageSize.orEmpty().split(",")
        .filter { it.isNotBlank() }.toMutableList()

    fun mediaResource() = when (jokePostParams.type) {
        "2" -> {
            jokePostParams.imageUrls?.split(",") ?: mutableListOf()
        }
        "3" -> {
            mutableListOf(jokePostParams.videoThumbnailUrl)
        }
        else -> {
            mutableListOf()
        }
    }
}

/**
 * @desc：
 * @date：2023/05/17 14:00
 */
sealed class JokePostAction {
    /** 发布帖子 */
    object PostJoke : JokePostAction()
}