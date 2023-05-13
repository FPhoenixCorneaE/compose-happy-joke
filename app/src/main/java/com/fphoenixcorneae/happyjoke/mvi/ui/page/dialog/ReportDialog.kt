package com.fphoenixcorneae.happyjoke.mvi.ui.page.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fphoenixcorneae.happyjoke.R
import com.fphoenixcorneae.happyjoke.mvi.ui.theme.Black30
import com.fphoenixcorneae.happyjoke.mvi.ui.widget.BottomSheetDialog

/**
 * @desc：举报弹窗
 * @date：2023/5/13 18:36
 * @param type 0-贴子列表 1-帖子详情
 */
@Preview
@Composable
fun ReportDialog(
    show: Boolean = false,
    type: Int = 0,
    onReportPromulgator: () -> Unit = {},
    onReportContent: () -> Unit = {},
    onUninterested: () -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    BottomSheetDialog(visible = show, modifier = Modifier.fillMaxSize(), onDismiss = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(color = Color.White)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .clickable {
                        onReportPromulgator()
                        onDismiss()
                    },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "举报发布者",
                    color = Black30,
                    fontSize = 14.sp,
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .clickable {
                        onReportContent()
                        onDismiss()
                    },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "举报此内容",
                    color = Black30,
                    fontSize = 14.sp,
                )
            }
            if (type == 0) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .clickable {
                            onUninterested()
                            onDismiss()
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "对此不感兴趣",
                        color = Black30,
                        fontSize = 14.sp,
                    )
                }
            }
            Divider(thickness = 8.dp, color = Color.Gray)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .clickable {
                        onDismiss()
                    },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(R.string.cancel),
                    color = Black30,
                    fontSize = 14.sp,
                )
            }
        }
    }
}