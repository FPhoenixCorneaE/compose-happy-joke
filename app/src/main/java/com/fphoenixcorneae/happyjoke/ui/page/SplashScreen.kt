package com.fphoenixcorneae.happyjoke.ui.page

import android.Manifest
import android.annotation.SuppressLint
import android.util.Log
import android.view.Window
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.fphoenixcorneae.happyjoke.R
import com.fphoenixcorneae.happyjoke.const.Constant
import com.fphoenixcorneae.happyjoke.ext.getDSFlow
import com.fphoenixcorneae.happyjoke.ui.page.dialog.PrivacyPolicyDialog
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.systemuicontroller.rememberSystemUiController

/**
 * @desc：
 * @date：2023/03/09 14:35
 */
@SuppressLint("FlowOperatorInvokedInComposition")
@Preview
@Composable
fun SplashScreen(window: Window? = null) {
    // 设置状态栏透明色
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !isSystemInDarkTheme()
    SideEffect {
        systemUiController.setStatusBarColor(color = Color.Transparent, darkIcons = useDarkIcons)
        window?.let {
            // 把内容延伸到状态栏
            WindowCompat.setDecorFitsSystemWindows(/* window = */ it,/* decorFitsSystemWindows = */ false)
        }
    }
    // 基于 LocalContext 获取 Context
    val context = LocalContext.current
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.mipmap.ic_logo),
            contentDescription = null,
            modifier = Modifier
                .align(alignment = Alignment.BottomCenter)
                .padding(bottom = 48.dp)
                .size(200.dp),
        )
        // 是否同意隐私政策
        val agreePrivacyPolicy by produceState(initialValue = false) {
            context.getDSFlow(Constant.Preferences.AGREE_PRIVACY_POLICY, false)
                .collect {
                    value = it
                }
        }
        if (agreePrivacyPolicy) {
            RequestPermissionsUsingAccompanist()
        } else {
            PrivacyPolicyDialog()
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun RequestPermissionsUsingAccompanist() {
    val permissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
        )
    )
    if (permissionsState.allPermissionsGranted) {
        Log.d("RequestPermissionsUsingAccompanist", "所有权限已授权")
    }
    SideEffect {
        permissionsState.launchMultiplePermissionRequest()
    }
}
