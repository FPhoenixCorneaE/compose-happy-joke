package com.fphoenixcorneae.happyjoke.startup

import android.app.Application
import android.content.Context
import androidx.startup.Initializer

/** Application上下文，全局可使用 */
val applicationContext by lazy { CommonInitializer.sApplication }

/**
 * @desc：
 * @date：2023/04/19 17:54
 */
class CommonInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        sApplication = context.applicationContext as Application
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }

    companion object {
        lateinit var sApplication: Application
    }
}