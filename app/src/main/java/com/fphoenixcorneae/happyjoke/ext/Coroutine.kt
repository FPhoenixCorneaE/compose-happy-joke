package com.fphoenixcorneae.happyjoke.ext

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/**
 * 生命周期与 Application 一样长的协程，可以做一些后台作业
 */
val globalScope by lazy {
    CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
}