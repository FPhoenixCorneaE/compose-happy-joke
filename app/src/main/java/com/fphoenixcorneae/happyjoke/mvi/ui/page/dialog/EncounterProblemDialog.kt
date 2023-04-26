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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fphoenixcorneae.happyjoke.R
import com.fphoenixcorneae.happyjoke.mvi.ui.theme.Black30
import com.fphoenixcorneae.happyjoke.mvi.ui.widget.BottomSheetDialog

/**
 * @desc：遇到问题弹窗
 * @date：2023/04/25 15:20
 * @param type 0-登录/注册 1-重置密码
 */
@Preview
@Composable
fun EncounterProblemDialog(
    show: Boolean = false,
    type: Int = 0,
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(R.string.encounter_problem_dialog_title),
                    color = Black30,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                )
            }
            if (type == 0) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .clickable {
                            onForgetPassword()
                            onDismiss()
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = stringResource(R.string.forget_password),
                        color = Black30,
                        fontSize = 14.sp,
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .clickable {
                        onContactCustomerService()
                        onDismiss()
                    },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(R.string.contact_customer_service),
                    color = Black30,
                    fontSize = 14.sp,
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .clickable {
                        onFeedback()
                        onDismiss()
                    },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(R.string.i_want_feedback),
                    color = Black30,
                    fontSize = 14.sp,
                )
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