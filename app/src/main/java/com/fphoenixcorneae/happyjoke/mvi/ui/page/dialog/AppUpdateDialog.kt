package com.fphoenixcorneae.happyjoke.mvi.ui.page.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.fphoenixcorneae.happyjoke.R
import com.fphoenixcorneae.happyjoke.ext.clickableNoRipple

/**
 * @desc：App更新弹窗
 * @date：2023/03/13 17:34
 */
@Preview
@Composable
fun AppUpdateDialog(onDismiss: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black.copy(alpha = 0.4f)),
    ) {
        Column(
            modifier = Modifier.align(alignment = Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .fillMaxHeight(0.5f)
            ) {
                AsyncImage(
                    model = R.mipmap.pic_bg_app_update,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                )
                Text(
                    text = "v3.1.1",
                    style = TextStyle(fontSize = 16.sp, color = Color.White),
                    modifier = Modifier.padding(start = 50.dp, top = 74.dp),
                )
                Text(
                    text = "1、修复Android 12 的问题\n2、修复搜索页面的问题\n3、加速内容审核",
                    style = TextStyle(fontSize = 16.sp, color = Color.Black),
                    modifier = Modifier.padding(start = 50.dp, top = 140.dp),
                )
                Button(
                    onClick = { },
                    modifier = Modifier
                        .padding(horizontal = 50.dp, vertical = 30.dp)
                        .align(alignment = Alignment.BottomCenter)
                        .fillMaxWidth()
                        .height(36.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onSecondary,
                        contentColor = Color.White,
                    ),
                ) {
                    Text(
                        text = stringResource(R.string.update_now),
                        style = TextStyle(fontSize = 16.sp),
                    )
                }
            }
            AsyncImage(
                model = R.mipmap.ic_close_circle,
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 28.dp)
                    .size(36.dp)
                    .clickableNoRipple {
                        onDismiss()
                    },
            )
        }
    }
}