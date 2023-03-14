package com.fphoenixcorneae.happyjoke.ui.page.home

import androidx.compose.runtime.*
import com.fphoenixcorneae.happyjoke.ui.page.dialog.AppUpdateDialog

/**
 * @desc：首页
 * @date：2023/03/13 17:31
 */
@Composable
fun HomepageScreen() {
    var showAppUpdateDialog by remember { mutableStateOf(true) }
    if (showAppUpdateDialog) {
        AppUpdateDialog(
            onDismiss = {
                showAppUpdateDialog = false
            },
        )
    }
}