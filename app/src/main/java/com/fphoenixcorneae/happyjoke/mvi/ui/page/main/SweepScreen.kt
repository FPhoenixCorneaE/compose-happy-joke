package com.fphoenixcorneae.happyjoke.mvi.ui.page.main

import android.view.Window
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fphoenixcorneae.happyjoke.mvi.ui.widget.SystemUiScaffold
import com.fphoenixcorneae.happyjoke.mvi.viewmodel.HomepageViewModel

/**
 * @desc：划一划仿抖音视频
 * @date：2023/03/17 10:46
 */
@Preview
@Composable
fun SweepScreen(
    window: Window? = null,
    viewModel: HomepageViewModel = viewModel(),
) {
    val coroutineScope = rememberCoroutineScope()
    SystemUiScaffold(
        window = window,
        isFitsSystemWindows = true,
        statusBarColor = MaterialTheme.colorScheme.onBackground,
    ) {

    }
}