package com.fphoenixcorneae.happyjoke.mvi.ui.page.main

import android.view.Window
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.fphoenixcorneae.happyjoke.const.Constant
import com.fphoenixcorneae.happyjoke.mvi.ui.page.dialog.AppUpdateDialog
import com.fphoenixcorneae.happyjoke.mvi.ui.widget.BottomNavigationBar
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

/**
 * @desc：主页
 * @date：2023/03/13 17:31
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainScreen(
    window: Window? = null,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val navController = rememberAnimatedNavController()
            AnimatedNavHost(
                navController = navController,
                startDestination = Constant.NavRoute.Main.HOMEPAGE,
                modifier = Modifier.fillMaxSize()
            ) {
                composable(
                    route = Constant.NavRoute.Main.HOMEPAGE,
                    enterTransition = { fadeIn() },
                    exitTransition = { fadeOut() },
                ) {
                    HomepageScreen(window = window)
                }
                composable(
                    route = Constant.NavRoute.Main.SWEEP,
                    enterTransition = { fadeIn() },
                    exitTransition = { fadeOut() },
                ) {
                    SweepScreen(window = window)
                }
                composable(
                    route = Constant.NavRoute.Main.MESSAGE,
                    enterTransition = { fadeIn() },
                    exitTransition = { fadeOut() },
                ) {
                    MessageScreen()
                }
                composable(
                    route = Constant.NavRoute.Main.MINE,
                    enterTransition = { fadeIn() },
                    exitTransition = { fadeOut() },
                ) {
                    MineScreen()
                }
            }
            BottomNavigationBar(
                modifier = Modifier.align(alignment = Alignment.BottomCenter),
                onCenterIconClick = {

                }
            ) {
                when (it) {
                    0 -> navController.navigate(Constant.NavRoute.Main.HOMEPAGE)
                    1 -> navController.navigate(Constant.NavRoute.Main.SWEEP)
                    2 -> navController.navigate(Constant.NavRoute.Main.MESSAGE)
                    else -> navController.navigate(Constant.NavRoute.Main.MINE)
                }
            }
        }
    }
    // App更新弹窗
    var showAppUpdateDialog by remember { mutableStateOf(false) }
    if (showAppUpdateDialog) {
        AppUpdateDialog(
            onDismiss = {
                showAppUpdateDialog = false
            },
        )
    }
}