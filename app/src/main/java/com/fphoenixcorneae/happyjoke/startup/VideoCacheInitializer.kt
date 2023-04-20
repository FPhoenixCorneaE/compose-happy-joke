package com.fphoenixcorneae.happyjoke.startup

import android.content.Context
import androidx.startup.Initializer

/**
 * @desc：
 * @date：2023/04/19 17:54
 */
class VideoCacheInitializer : Initializer<Unit> {

    override fun create(context: Context) {
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}