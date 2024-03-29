@file:OptIn(ExperimentalAnimationApi::class)

package com.fphoenixcorneae.happyjoke.mvi.ui.page.splash

import android.Manifest
import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.fphoenixcorneae.happyjoke.R
import com.fphoenixcorneae.happyjoke.const.Constant
import com.fphoenixcorneae.happyjoke.ext.getDSFlow
import com.fphoenixcorneae.happyjoke.mvi.ui.page.dialog.PrivacyPolicyDialog
import com.fphoenixcorneae.happyjoke.mvi.ui.widget.SystemUiScaffold
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

/**
 * @desc：
 * @date：2023/03/09 14:35
 */
@SuppressLint("FlowOperatorInvokedInComposition")
@Preview
@Composable
fun SplashScreen(
    navController: NavHostController = rememberAnimatedNavController(),
) {
    SystemUiScaffold(statusBarsPadding = false, isDarkFont = false) {
        // 基于 LocalContext 获取 Context
        val context = LocalContext.current
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.mipmap.ic_logo),
                contentDescription = null,
                modifier = Modifier
                    .align(alignment = Alignment.BottomCenter)
                    .padding(bottom = 36.dp)
                    .size(160.dp),
            )
            // 是否同意隐私政策
            val agreePrivacyPolicy by produceState<Boolean?>(initialValue = null) {
                context.getDSFlow<Boolean?>(Constant.Preferences.AGREE_PRIVACY_POLICY, false)
                    .collect {
                        value = it
                    }
            }
            agreePrivacyPolicy?.let {
                if (it) {
                    RequestPermissionsUsingAccompanist {
                        navController.navigate(Constant.NavRoute.MAIN)
                    }
                } else {
                    PrivacyPolicyDialog(navController = navController)
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun RequestPermissionsUsingAccompanist(
    onAllPermissionsGranted: () -> Unit = {},
) {
    val permissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
        )
    )
    if (permissionsState.allPermissionsGranted) {
        onAllPermissionsGranted()
    }
    SideEffect {
        permissionsState.launchMultiplePermissionRequest()
    }
}
