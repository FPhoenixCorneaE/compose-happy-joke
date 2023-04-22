package com.fphoenixcorneae.happyjoke.mvi.ui.page.login

import android.content.Intent
import android.net.Uri
import android.view.Window
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.fphoenixcorneae.happyjoke.R
import com.fphoenixcorneae.happyjoke.const.Constant
import com.fphoenixcorneae.happyjoke.ext.noRippleClickable
import com.fphoenixcorneae.happyjoke.mvi.ui.theme.Grey70
import com.fphoenixcorneae.happyjoke.mvi.ui.theme.Yellow30
import com.fphoenixcorneae.happyjoke.mvi.ui.widget.AccountEditText
import com.fphoenixcorneae.happyjoke.mvi.ui.widget.AuthCodeEditText
import com.fphoenixcorneae.happyjoke.mvi.ui.widget.SystemUiScaffold
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

/**
 * @desc：登录
 * @date：2023/04/21 15:56
 */
@OptIn(ExperimentalAnimationApi::class)
@Preview(showSystemUi = true, showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun LoginScreen(
    window: Window? = null,
    navController: NavHostController = rememberAnimatedNavController(),
) {
    val context = LocalContext.current
    SystemUiScaffold(window = window) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            AsyncImage(
                model = R.mipmap.ic_close_black,
                contentDescription = null,
                modifier = Modifier
                    .size(20.dp)
                    .noRippleClickable {
                        navController.navigateUp()
                    },
            )
            var isAuthCodeLogin by remember { mutableStateOf(true) }
            Text(
                text = stringResource(if (isAuthCodeLogin) R.string.auth_code_login else R.string.password_login),
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 20.dp),
            )
            var account by rememberSaveable { mutableStateOf("") }
            var password by rememberSaveable { mutableStateOf("") }
            AccountEditText(
                onValueChange = { account = it },
                text = account,
                textStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold),
                hint = "请输入手机号",
                modifier = Modifier
                    .padding(top = 40.dp)
                    .fillMaxWidth()
                    .height(40.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
                ),
            )
            var authCode by rememberSaveable { mutableStateOf("") }
            if (isAuthCodeLogin) {
                AuthCodeEditText(
                    onValueChange = { authCode = it },
                    text = authCode,
                    textStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold),
                    hint = "请输入验证码",
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth()
                        .height(40.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
                    ),
                )
            } else {
                AccountEditText(
                    onValueChange = { password = it },
                    text = password,
                    textStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold),
                    hint = "请输入密码",
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth()
                        .height(40.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
                    ),
                )
            }
            // 登录
            Button(
                onClick = { },
                modifier = Modifier
                    .padding(top = 32.dp)
                    .fillMaxWidth()
                    .height(40.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Yellow30,
                    contentColor = Color.White,
                ),
            ) {
                Text(
                    text = stringResource(R.string.login),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
            Box(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(if (isAuthCodeLogin) R.string.password_login else R.string.auth_code_login),
                    color = Yellow30,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .noRippleClickable {
                            isAuthCodeLogin = !isAuthCodeLogin
                        },
                )
                Text(
                    text = "遇到问题？",
                    color = Yellow30,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .noRippleClickable {

                        },
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.dp)
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = "其他登录方式",
                    color = Grey70,
                    fontSize = 11.sp,
                )
                Row(
                    modifier = Modifier.padding(top = 30.dp),
                    horizontalArrangement = Arrangement.spacedBy(40.dp)
                ) {
                    AsyncImage(
                        model = R.mipmap.ic_share_mode_qq,
                        contentDescription = null,
                        modifier = Modifier
                            .size(36.dp)
                            .noRippleClickable {

                            },
                    )
                    AsyncImage(
                        model = R.mipmap.ic_share_mode_sina,
                        contentDescription = null,
                        modifier = Modifier
                            .size(36.dp)
                            .noRippleClickable {

                            },
                    )
                    AsyncImage(
                        model = R.mipmap.ic_login_shortcut,
                        contentDescription = null,
                        modifier = Modifier
                            .size(36.dp)
                            .noRippleClickable {

                            },
                    )
                }
                ClickableText(
                    text = buildAnnotatedString {
                        append("登录/注册代表您同意段子乐")
                        withStyle(
                            SpanStyle(color = Yellow30)
                        ) {
                            append(stringResource(R.string.privacy_policy_protocol))
                        }
                        append(stringResource(R.string.privacy_policy_tips_str_2))
                        withStyle(
                            SpanStyle(color = Yellow30)
                        ) {
                            append(stringResource(R.string.user_service_protocol))
                        }
                    },
                    style = TextStyle(
                        color = Grey70,
                        fontSize = 11.sp,
                        lineHeight = 20.sp,
                        textAlign = TextAlign.Center,
                    ),
                    modifier = Modifier
                        .width(208.dp)
                        .padding(top = 30.dp),
                ) {
                    when (it) {
                        in 13..18 -> {
                            // 点击了《隐私政策》
                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Constant.WEB_PRIVACY)))
                        }
                        in 20..27 -> {
                            // 点击了《用户服务协议》
                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Constant.WEB_PROTO)))
                        }
                    }
                }
            }
        }
    }
}