package com.fphoenixcorneae.happyjoke.https

import android.net.ParseException
import com.google.gson.JsonParseException
import com.google.gson.stream.MalformedJsonException
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException

/**
 * @desc：异常统一处理
 * @date：2023/04/17 16:39
 */
object ExceptionHandling {
    /** 未知错误 */
    private const val UNKNOWN_ERROR_CODE = -1000
    private const val UNKNOWN_ERROR = "未知错误"

    /** 网络连接错误 */
    private const val NETWORK_ERROR_CODE = -1001
    private const val NETWORK_ERROR = "网络连接错误"

    /** 网络连接超时 */
    private const val TIMEOUT_ERROR_CODE = -1002
    private const val TIMEOUT_ERROR = "网络连接超时"

    /** 解析错误 */
    private const val PARSE_ERROR_CODE = -1003
    private const val PARSE_ERROR = "解析错误"

    /** 证书验证失败 */
    private const val SSL_ERROR_CODE = -1004
    private const val SSL_ERROR = "证书验证失败"

    fun deal(e: Throwable?): Throwable {
        return when (e) {
            is HttpException -> ApiException(code = e.code(), msg = NETWORK_ERROR, cause = e)
            is JsonParseException, is JSONException, is ParseException, is MalformedJsonException -> ApiException(
                code = PARSE_ERROR_CODE,
                msg = PARSE_ERROR,
                cause = e
            )
            is ConnectException -> ApiException(code = NETWORK_ERROR_CODE, msg = NETWORK_ERROR, cause = e)
            is javax.net.ssl.SSLException -> ApiException(code = SSL_ERROR_CODE, msg = SSL_ERROR, cause = e)
            is ConnectTimeoutException, is java.net.SocketTimeoutException, is java.net.UnknownHostException -> ApiException(
                code = TIMEOUT_ERROR_CODE,
                msg = TIMEOUT_ERROR,
                cause = e
            )
            is ServerException -> e
            else -> ApiException(code = UNKNOWN_ERROR_CODE, msg = UNKNOWN_ERROR, cause = e)
        }
    }
}

/**
 * @desc：
 * @date：2023/04/17 17:17
 */
data class ApiException(
    val code: Int,
    val msg: String? = null,
    override val cause: Throwable? = null,
) : Exception()

/**
 * @desc：
 * @date：2023/04/17 17:17
 */
data class ServerException(
    val code: Int,
    val msg: String? = null,
) : Exception()