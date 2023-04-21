package com.fphoenixcorneae.happyjoke.mvi.ui.page

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.fphoenixcorneae.happyjoke.const.Constant
import com.fphoenixcorneae.happyjoke.mvi.ui.page.joke.JokePostScreen
import com.fphoenixcorneae.happyjoke.mvi.ui.page.login.LoginScreen
import com.fphoenixcorneae.happyjoke.mvi.ui.page.main.MainScreen
import com.fphoenixcorneae.happyjoke.mvi.ui.page.splash.SplashScreen
import com.fphoenixcorneae.happyjoke.mvi.ui.theme.ComposeHappyJokeTheme
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

/**
 * @desc：
 * @date：2023/03/09 13:39
 */
@OptIn(ExperimentalAnimationApi::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeHappyJokeTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val navController = rememberAnimatedNavController()
                    AnimatedNavHost(
                        navController = navController,
                        startDestination = Constant.NavRoute.SPLASH,
                    ) {
                        composable(
                            route = Constant.NavRoute.SPLASH,
                            enterTransition = { fadeIn() },
                            exitTransition = { fadeOut() },
                        ) {
                            SplashScreen(window = window, navController)
                        }
                        composable(
                            Constant.NavRoute.MAIN,
                            enterTransition = { fadeIn() },
                            exitTransition = { fadeOut() },
                            popEnterTransition = { fadeIn() },
                            popExitTransition = { fadeOut() },
                        ) {
                            MainScreen(window = window, navController)
                        }
                        composable(
                            Constant.NavRoute.JOKE_POST,
                            enterTransition = { slideInVertically(initialOffsetY = { it }) + fadeIn() },
                            exitTransition = { slideOutVertically(targetOffsetY = { it }) + fadeOut() },
                            popEnterTransition = { slideInVertically(initialOffsetY = { it }) + fadeIn() },
                            popExitTransition = { slideOutVertically(targetOffsetY = { it }) + fadeOut() },
                        ) {
                            JokePostScreen(window = window, navController)
                        }
                        composable(
                            Constant.NavRoute.LOGIN,
                            enterTransition = { slideInVertically(initialOffsetY = { it }) + fadeIn() },
                            exitTransition = { slideOutVertically(targetOffsetY = { it }) + fadeOut() },
                            popEnterTransition = { slideInVertically(initialOffsetY = { it }) + fadeIn() },
                            popExitTransition = { slideOutVertically(targetOffsetY = { it }) + fadeOut() },
                        ) {
                            LoginScreen(window = window, navController)
                        }
                    }
                }
            }
        }
    }
}