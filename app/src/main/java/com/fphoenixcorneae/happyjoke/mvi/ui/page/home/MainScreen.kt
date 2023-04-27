package com.fphoenixcorneae.happyjoke.mvi.ui.page.home

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.fphoenixcorneae.happyjoke.const.Constant
import com.fphoenixcorneae.happyjoke.mvi.ui.page.dialog.AppUpdateDialog
import com.fphoenixcorneae.happyjoke.mvi.ui.widget.BottomNavigationBar
import com.fphoenixcorneae.happyjoke.tool.UserManager
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
    navController: NavHostController = rememberAnimatedNavController(),
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
            val animatedNavController = rememberAnimatedNavController()
            AnimatedNavHost(
                navController = animatedNavController,
                startDestination = Constant.NavRoute.Main.HOMEPAGE,
                modifier = Modifier.fillMaxSize()
            ) {
                composable(
                    route = Constant.NavRoute.Main.HOMEPAGE,
                    enterTransition = { fadeIn() },
                    exitTransition = { fadeOut() },
                ) {
                    HomepageScreen()
                }
                composable(
                    route = Constant.NavRoute.Main.SWEEP,
                    enterTransition = { fadeIn() },
                    exitTransition = { fadeOut() },
                ) {
                    SweepScreen()
                }
                composable(
                    route = Constant.NavRoute.Main.MESSAGE,
                    enterTransition = { fadeIn() },
                    exitTransition = { fadeOut() },
                ) {
                    MessageScreen()
                }
                composable(
                    route = Constant.NavRoute.Main.ME,
                    enterTransition = { fadeIn() },
                    exitTransition = { fadeOut() },
                ) {
                    MeScreen(navHostController = navController)
                }
            }
            var currentPosition by rememberSaveable { mutableStateOf(0) }
            BottomNavigationBar(
                modifier = Modifier.align(alignment = Alignment.BottomCenter),
                currentPosition = currentPosition,
                onCenterIconClick = {
                    navController.navigate(Constant.NavRoute.JOKE_POST)
                }
            ) {
                currentPosition = it
                when (it) {
                    0 -> animatedNavController.navigate(Constant.NavRoute.Main.HOMEPAGE)
                    1 -> animatedNavController.navigate(Constant.NavRoute.Main.SWEEP)
                    3 -> {
                        if (!UserManager.isLoggedIn()) {
                            navController.navigate(Constant.NavRoute.LOGIN)
                            currentPosition = 0
                            animatedNavController.navigate(Constant.NavRoute.Main.HOMEPAGE)
                        } else {
                            animatedNavController.navigate(Constant.NavRoute.Main.MESSAGE)
                        }
                    }
                    else -> animatedNavController.navigate(Constant.NavRoute.Main.ME)
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