package com.fphoenixcorneae.happyjoke.mvi.ui.page.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fphoenixcorneae.happyjoke.mvi.ui.theme.Black30
import com.fphoenixcorneae.happyjoke.mvi.ui.widget.BottomSheetDialog

/**
 * @desc：遇到问题弹窗
 * @date：2023/04/25 15:20
 */
@Preview
@Composable
fun EncounterProblemDialog(
    show: Boolean = false,
    onForgetPassword: () -> Unit = {},
    onContactCustomerService: () -> Unit = {},
    onFeedback: () -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    BottomSheetDialog(visible = show, modifier = Modifier.fillMaxSize(), onDismiss = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(color = Color.White)
        ) {
            Text(
                text = "请选择您遇到的问题",
                color = Black30,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
            )
            Text(
                text = "忘记密码",
                color = Black30,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .clickable {
                        onForgetPassword()
                        onDismiss()
                    },
            )
            Text(
                text = "联系客服",
                color = Black30,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .clickable {
                        onContactCustomerService()
                        onDismiss()
                    },
            )
            Text(
                text = "我要反馈",
                color = Black30,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .clickable {
                        onFeedback()
                        onDismiss()
                    },
            )
            Divider(thickness = 8.dp, color = Color.Gray)
            Text(
                text = "取消",
                color = Black30,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .clickable {
                        onDismiss()
                    },
            )
        }
    }
}