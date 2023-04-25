package com.fphoenixcorneae.happyjoke.mvi.ui.page.dialog

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fphoenixcorneae.happyjoke.R
import com.fphoenixcorneae.happyjoke.const.Constant
import com.fphoenixcorneae.happyjoke.ext.clickableNoRipple
import com.fphoenixcorneae.happyjoke.ext.saveDS
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

/**
 * @desc：隐私政策弹窗
 * @date：2023/03/09 15:33
 */
@Preview
@Composable
fun PrivacyPolicyDialog() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black.copy(alpha = 0.4f)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .wrapContentHeight()
                .background(color = MaterialTheme.colorScheme.background, shape = RoundedCornerShape(16.dp))
                .align(alignment = Alignment.Center)
        ) {
            Text(
                text = stringResource(R.string.privacy_policy),
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 22.sp,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 20.dp),
            )
            Text(
                text = stringResource(R.string.privacy_policy_prompt),
                style = TextStyle(
                    color = Color.Gray,
                    fontSize = 18.sp,
                    lineHeight = 30.sp,
                    // 段落内容缩进
                    textIndent = TextIndent(36.sp),
                ),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(start = 20.dp, top = 20.dp, end = 20.dp),
            )
            ClickableText(
                text = buildAnnotatedString {
                    append(stringResource(R.string.privacy_policy_tips_str_1))
                    withStyle(
                        SpanStyle(color = MaterialTheme.colorScheme.primary)
                    ) {
                        append(stringResource(R.string.privacy_policy_protocol))
                    }
                    append(stringResource(R.string.privacy_policy_tips_str_2))
                    withStyle(
                        SpanStyle(color = MaterialTheme.colorScheme.primary)
                    ) {
                        append(stringResource(R.string.user_protocol))
                    }
                    append(stringResource(R.string.privacy_policy_tips_str_3))
                },
                style = TextStyle(
                    color = Color.Gray,
                    fontSize = 18.sp,
                    lineHeight = 30.sp,
                    // 段落内容缩进
                    textIndent = TextIndent(36.sp),
                ),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(start = 20.dp, top = 20.dp, end = 20.dp),
            ) {
                when (it) {
                    in 5..10 -> {
                        // 点击了《隐私政策》
                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Constant.WEB_PRIVACY)))
                    }
                    in 12..17 -> {
                        // 点击了《用户协议》
                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Constant.WEB_PROTO)))
                    }
                }
            }
            Divider(
                modifier = Modifier.padding(top = 16.dp),
                color = Color.LightGray,
                thickness = 0.5.dp,
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(0.dp)
                        .weight(weight = 1f)
                        .clickableNoRipple {
                            // 退出app
                            exitProcess(0)
                        }
                ) {
                    Text(
                        text = stringResource(R.string.refuse),
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 20.sp,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier
                            .align(alignment = Alignment.Center),
                    )
                }
                Box(
                    modifier = Modifier
                        .width(0.5.dp)
                        .fillMaxHeight()
                        .background(color = Color.LightGray)
                )
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(0.dp)
                        .weight(weight = 1f)
                        .clickableNoRipple {
                            coroutineScope.launch {
                                context.saveDS(Constant.Preferences.AGREE_PRIVACY_POLICY, true)
                            }
                        }
                ) {
                    Text(
                        text = stringResource(R.string.agree),
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 20.sp,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier
                            .align(alignment = Alignment.Center),
                    )
                }
            }
        }
    }
}