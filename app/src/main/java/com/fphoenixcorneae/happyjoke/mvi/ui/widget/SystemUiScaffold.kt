package com.fphoenixcorneae.happyjoke.mvi.ui.widget

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController

/**
 * @desc：适配状态栏、底部导航栏
 * @date：2023/03/17 11:40
 * @param isFitsSystemWindows   默认false，沉浸式状态栏
 * @param statusBarsPadding     是否给状态栏预留高度，以免遮盖UI
 * @param navigationBarsPadding 是否给底部状态栏预留高度，以免遮盖UI
 * @param statusBarColor        设置顶部状态栏的颜色
 * @param navigationBarColor    设置底部状态栏的颜色
 * @param isDarkFont            状态栏内容是否为黑色
 */
@Composable
inline fun SystemUiScaffold(
    modifier: Modifier = Modifier,
    isFitsSystemWindows: Boolean = false,
    statusBarsPadding: Boolean = true,
    navigationBarsPadding: Boolean = true,
    statusBarColor: Color = Color.Transparent,
    navigationBarColor: Color = Color.Transparent,
    isDarkFont: Boolean = !isSystemInDarkTheme(),
    crossinline content: @Composable () -> Unit,
) {
    val activity = LocalContext.current as? Activity
    activity?.window?.let {
        // 沉浸式状态栏设置
        WindowCompat.setDecorFitsSystemWindows(/* window = */ it, /* decorFitsSystemWindows = */ isFitsSystemWindows)
    }
    val systemUiController = rememberSystemUiController()
    SideEffect {
        // 设置顶部状态栏的颜色
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = isDarkFont,
        )
        // 设置底部状态栏的颜色
        systemUiController.setNavigationBarColor(
            color = navigationBarColor,
            darkIcons = isDarkFont,
        )
    }
    // 在方法参数里面添加了 Modifier就是为了可以设置background(color,shape)同时还可以设置渐变色background(brush, shape)
    var modifierNew: Modifier = modifier.then(Modifier.fillMaxSize())

    if (!isFitsSystemWindows && systemUiController.isStatusBarVisible) {
        // 如果是沉浸式需要设置 padding来占位
        modifierNew = modifierNew.then(Modifier.imePadding())
    }
    if (!isFitsSystemWindows && statusBarsPadding && systemUiController.isStatusBarVisible) {
        // 给状态栏预留高度，以免遮盖UI
        modifierNew = modifierNew.then(Modifier.statusBarsPadding())
    }
    if (navigationBarsPadding && systemUiController.isNavigationBarVisible) {
        // 给底部状态栏预留高度，以免遮盖UI
        modifierNew = modifierNew.then(Modifier.navigationBarsPadding())
    }

    Box(
        modifier = modifierNew
    ) {
        content()
    }
}