package com.fphoenixcorneae.happyjoke.mvi.viewmodel

import android.graphics.BitmapFactory
import android.util.Log
import com.fphoenixcorneae.happyjoke.ext.launchDefault
import com.fphoenixcorneae.happyjoke.ext.launchIo
import com.fphoenixcorneae.happyjoke.ext.toJson
import com.fphoenixcorneae.happyjoke.ext.toObject
import com.fphoenixcorneae.happyjoke.https.doOnError
import com.fphoenixcorneae.happyjoke.https.doOnSuccess
import com.fphoenixcorneae.happyjoke.https.helperService
import com.fphoenixcorneae.happyjoke.https.httpRequest
import com.fphoenixcorneae.happyjoke.https.jokeService
import com.fphoenixcorneae.happyjoke.https.request.JokePostParams
import com.fphoenixcorneae.happyjoke.mvi.model.BaseReply
import com.fphoenixcorneae.happyjoke.tool.QiNiuHelper
import com.google.gson.reflect.TypeToken
import com.qiniu.android.storage.UploadManager
import com.qiniu.android.storage.UploadOptions
import github.leavesczy.matisse.MediaResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import java.io.File
import java.net.URI
import java.util.UUID
import java.util.concurrent.CountDownLatch

/**
 * @desc：
 * @date：2023/05/17 14:00
 */
class JokePostViewModel : BaseViewModel<JokePostAction>() {

    private val _jokePostUiState = MutableStateFlow(JokePostUiState())
    val jokePostUiState = _jokePostUiState.asStateFlow()
    private val uploadManager by lazy { UploadManager() }
    private var isCancelled = false

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
                val imageResult = it.result()
                result.forEach { mediaResource ->
                    imageUrls.add(mediaResource.uri.toString())
                    val options = BitmapFactory.Options()
                    options.inJustDecodeBounds = true
                    BitmapFactory.decodeFile(File(mediaResource.uri.toString()).absolutePath, options)
                    imageSize.add("${options.outWidth}x${options.outHeight}")
                    imageResult.add(mediaResource)
                }
                it.copy(
                    jokePostParams = it.jokePostParams.copy(
                        imageUrls = imageUrls.joinToString(","),
                        imageSize = imageSize.joinToString(","),
                        result = imageResult,
                    )
                )
            }
        }
    }

    fun removeMediaResource(index: Int) {
        launchDefault {
            if (
                (_jokePostUiState.first().postType() == JokePostUiState.POST_TYPE_IMAGE
                        && _jokePostUiState.first().imageUrls().size == 1)
                || _jokePostUiState.first().postType() == JokePostUiState.POST_TYPE_VIDEO
            ) {
                textType()
            } else {
                _jokePostUiState.update {
                    val imageUrls = it.imageUrls()
                    val imageSize = it.imageSize()
                    val imageResult = it.result()
                    imageUrls.removeAt(index)
                    imageSize.removeAt(index)
                    imageResult.removeAt(index)
                    it.copy(
                        jokePostParams = it.jokePostParams.copy(
                            imageUrls = imageUrls.joinToString(","),
                            imageSize = imageSize.joinToString(","),
                            result = imageResult,
                        )
                    )
                }
            }
        }
    }

    fun textType() {
        launchDefault {
            _jokePostUiState.update {
                it.copy(
                    jokePostParams = JokePostParams(
                        content = it.jokePostParams.content,
                        type = JokePostUiState.POST_TYPE_TEXT,
                    )
                )
            }
        }
    }

    fun imageType() {
        launchDefault {
            _jokePostUiState.update {
                it.copy(
                    jokePostParams = it.jokePostParams.copy(
                        type = JokePostUiState.POST_TYPE_IMAGE,
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
                        type = JokePostUiState.POST_TYPE_VIDEO,
                        imageUrls = "",
                        imageSize = "",
                        result = mutableListOf(),
                    )
                )
            }
        }
    }

    override fun dealIntent(action: JokePostAction) {
        when (action) {
            JokePostAction.PostJoke -> launchIo {
                _jokePostUiState.first().apply {
                    if (postType() == JokePostUiState.POST_TYPE_IMAGE) {
                        val countDownLatch = CountDownLatch(imageUrls().size)
                        result().forEach {
                            val filename = StringBuilder()
                                .append(UUID.randomUUID().toString().replace("-", ""))
                                .append(".${it.mimeType.split("/")[1]}")
                                .toString()
                            val key = StringBuilder()
                                .append(QiNiuHelper.getBucket())
                                .append(filename)
                                .toString()
                            Log.i("uploadManager", "key: $key")
                            val qiNiuToken = withContext(Dispatchers.IO) {
                                helperService.getQiNiuToken(
                                    type = 0,
                                    filename = filename,
                                )?.data?.token
                            }
                            withContext(Dispatchers.IO) {
                                uploadManager.put(
                                    /* filePath = */
                                    it.path,
                                    /* key = */
                                    key,
                                    /* token = */
                                    qiNiuToken,
                                    /* completionHandler = */
                                    { key, info, response ->
                                        Log.i("uploadManager", "completionHandler: $key,\r\n$info,\r\n$response")
                                        // 标记已经完成一个任务
                                        countDownLatch.countDown()
                                        if (info.isOK) {
                                        }
                                    },
                                    /* options = */
                                    UploadOptions(
                                        /* params = */
                                        null,
                                        /* mimeType = */
                                        null,
                                        /* checkCrc = */
                                        false,
                                        /* progressHandler = */
                                        { key, percent ->
                                            Log.i("uploadManager", "progressHandler: $key,\r\n$percent")
                                        },
                                        /* cancellationSignal = */
                                        { isCancelled },
                                    ),
                                )
                            }
                        }
                        // 主线程等待所有统计指标执行完毕
                        countDownLatch.await()
                    }
                }
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

    override fun onCleared() {
        isCancelled = true
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
    val uploadProgress: Double = 0.0,
) {

    fun postType() = jokePostParams.type

    fun isContentBlank() = jokePostParams.content.isBlank()

    fun isContentExceedLimit() = jokePostParams.content.length > 300

    fun imageUrls() = jokePostParams.imageUrls.orEmpty().split(",")
        .filter { it.isNotBlank() }.toMutableList()

    fun imageSize() = jokePostParams.imageSize.orEmpty().split(",")
        .filter { it.isNotBlank() }.toMutableList()

    fun result() = jokePostParams.result.toMutableList()

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

    companion object {
        const val POST_TYPE_TEXT = "1"
        const val POST_TYPE_IMAGE = "2"
        const val POST_TYPE_VIDEO = "3"
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