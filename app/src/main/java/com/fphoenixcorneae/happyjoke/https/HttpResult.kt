package com.fphoenixcorneae.happyjoke.https

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

/**
 * @desc：
 * @date：2023/04/17 15:41
 */
sealed class HttpResult<out T> {
    data class Loading(val msg: CharSequence?) : HttpResult<Nothing>()
    data class Success<out T>(val data: T) : HttpResult<T>()
    data class Error(val t: Throwable) : HttpResult<Nothing>()
}

/**
 * 发送 Http 请求
 */
suspend inline fun <reified T> httpRequest(
    loadingMsg: CharSequence? = null,
    crossinline block: suspend () -> T,
): Flow<HttpResult<T>> = flow {
    runCatching {
        block()
    }.onSuccess {
        emit(HttpResult.Success(it))
    }.onFailure {
        emit(HttpResult.Error(ExceptionHandling.deal(it)))
    }
}.onStart {
    emit(HttpResult.Loading(loadingMsg))
}

suspend inline fun <reified T> Flow<HttpResult<T>>.send() = apply {
    collect()
}

suspend inline fun <reified T> Flow<HttpResult<T>>.doOnLoading(
    crossinline block: (CharSequence?) -> Unit,
) = apply {
    onEach {
        if (it is HttpResult.Loading) {
            withContext(Dispatchers.Main) {
                block(it.msg)
            }
        }
    }
}

suspend inline fun <reified T> Flow<HttpResult<T>>.doOnSuccess(
    crossinline block: (T) -> Unit,
) = apply {
    onEach {
        if (it is HttpResult.Success) {
            withContext(Dispatchers.Main) {
                block(it.data)
            }
        }
    }
}

suspend inline fun <reified T> Flow<HttpResult<T>>.doOnError(
    crossinline block: (Throwable) -> Unit,
) = apply {
    onEach {
        if (it is HttpResult.Error) {
            withContext(Dispatchers.Main) {
                block(it.t)
            }
        }
    }
}
