package com.fphoenixcorneae.happyjoke.mvi.ui.page.home

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.fphoenixcorneae.happyjoke.mvi.ui.page.dialog.AppUpdateDialog

/**
 * @desc：首页
 * @date：2023/03/13 17:31
 */
@Composable
fun HomepageScreen() {
    Box {
        BottomNavigationBar(modifier = Modifier.align(alignment = Alignment.BottomCenter))
    }
    var showAppUpdateDialog by remember { mutableStateOf(false) }
    if (showAppUpdateDialog) {
        AppUpdateDialog(
            onDismiss = {
                showAppUpdateDialog = false
            },
        )
    }
}