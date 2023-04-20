package com.fphoenixcorneae.happyjoke.exoplayer

import android.content.Context
import com.danikula.videocache.HttpProxyCacheServer
import java.io.File

/**
 * @desc：
 * @date：2023/04/20 09:32
 */
object HttpProxyCacheManager {

    private const val PATH_VIDEO_CACHE = "video-cache"

    @Volatile
    private var sHttpProxyCacheServer: HttpProxyCacheServer? = null

    fun getProxy(context: Context): HttpProxyCacheServer {
        return sHttpProxyCacheServer ?: synchronized(this) {
            sHttpProxyCacheServer ?: newProxy(context).also { sHttpProxyCacheServer = it }
        }
    }

    fun getProxyUrl(context: Context, url: String?, allowCachedFileUri: Boolean = true): String? {
        return runCatching {
            getProxy(context).getProxyUrl(url, allowCachedFileUri)
        }.onFailure { it.printStackTrace() }.getOrNull()
    }

    private fun newProxy(context: Context): HttpProxyCacheServer {
        return HttpProxyCacheServer.Builder(context)
            .cacheDirectory(getVideoCacheDir(context))
            .build()
    }

    private fun getVideoCacheDir(context: Context): File {
        return File(context.externalCacheDir, PATH_VIDEO_CACHE)
    }
}