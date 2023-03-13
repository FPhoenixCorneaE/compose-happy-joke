package com.fphoenixcorneae.happyjoke.ui.page.dialog

import android.util.Log
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
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.fphoenixcorneae.happyjoke.const.Constant
import com.fphoenixcorneae.happyjoke.ext.dataStore
import com.fphoenixcorneae.happyjoke.ext.noRippleClickable
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
                text = "隐私政策",
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
                text = "尊敬的用户您好，段子乐根据最新的法律法规及监管政策要求，向您推送本提示。为了更好的通知您新的消息，我们需要您为本应用授权通知权限。为了发布图片和视频内容，需要您授权相机权限。为了更精准的推荐内容，需要您同意设备信息收集授权。以及为了更加稳定的提供服务，需要收集一些日志信息。",
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
                    append("您可以点击")
                    withStyle(
                        SpanStyle(color = MaterialTheme.colorScheme.primary)
                    ) {
                        append("《隐私政策》")
                    }
                    append("和")
                    withStyle(
                        SpanStyle(color = MaterialTheme.colorScheme.primary)
                    ) {
                        append("《用户协议》")
                    }
                    append("阅读完整版条款内容。如您同意，请点击“同意”开始接受我们的服务。")
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
                        Log.d("ClickableText", "点击了《隐私政策》")
                    }
                    in 12..17 -> {
                        // 点击了《用户协议》
                        Log.d("ClickableText", "点击了《用户协议》")
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
                        .noRippleClickable {
                            // 退出app
                            exitProcess(0)
                        }
                ) {
                    Text(
                        text = "拒绝",
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
                        .noRippleClickable {
                            coroutineScope.launch {
                                context.saveDS(Constant.Preferences.AGREE_PRIVACY_POLICY, true)
                            }
                        }
                ) {
                    Text(
                        text = "同意",
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