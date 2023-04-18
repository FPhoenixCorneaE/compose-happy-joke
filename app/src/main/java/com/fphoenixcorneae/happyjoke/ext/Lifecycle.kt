package com.fphoenixcorneae.happyjoke.ext

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

/**
 * 生命周期观察者
 */
@Composable
fun LifecycleObserver(
    onCreate: () -> Unit = {},
    onStart: () -> Unit = {},
    onResume: () -> Unit = {},
    onPause: () -> Unit = {},
    onStop: () -> Unit = {},
    onDestroy: () -> Unit = {},
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    // 当提供了一个新的lambdas时，可以安全地更新当前的lambdas
    val create by rememberUpdatedState(onCreate)
    val start by rememberUpdatedState(onStart)
    val resume by rememberUpdatedState(onResume)
    val pause by rememberUpdatedState(onPause)
    val stop by rememberUpdatedState(onStop)
    val destroy by rememberUpdatedState(onDestroy)
    // 如果“lifecycleOwner”改变，处置并重置效果
    DisposableEffect(key1 = lifecycleOwner) {
        // 创建一个观察者来触发我们所记得的回调来发送事件
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> create()
                Lifecycle.Event.ON_START -> start()
                Lifecycle.Event.ON_RESUME -> resume()
                Lifecycle.Event.ON_PAUSE -> pause()
                Lifecycle.Event.ON_STOP -> stop()
                Lifecycle.Event.ON_DESTROY -> destroy()
                else -> {}
            }
        }
        // 将观察者添加到生命周期中
        lifecycleOwner.lifecycle.addObserver(observer)
        // 当组合函数离开时，会执行
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}