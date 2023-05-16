package com.fphoenixcorneae.happyjoke.mvi.ui.page.user

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.fphoenixcorneae.happyjoke.R
import com.fphoenixcorneae.happyjoke.const.Constant
import com.fphoenixcorneae.happyjoke.ext.clickableNoRipple
import com.fphoenixcorneae.happyjoke.ext.toast
import com.fphoenixcorneae.happyjoke.mvi.ui.page.dialog.EncounterProblemDialog
import com.fphoenixcorneae.happyjoke.mvi.ui.theme.Grey70
import com.fphoenixcorneae.happyjoke.mvi.ui.theme.Yellow30
import com.fphoenixcorneae.happyjoke.mvi.ui.widget.AccountEditText
import com.fphoenixcorneae.happyjoke.mvi.ui.widget.AuthCodeEditText
import com.fphoenixcorneae.happyjoke.mvi.ui.widget.SystemUiScaffold
import com.fphoenixcorneae.happyjoke.mvi.viewmodel.PasswordAction
import com.fphoenixcorneae.happyjoke.mvi.viewmodel.PasswordViewModel
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

/**
 * @desc：重置密码
 * @date：2023/04/25 14:40
 */
@OptIn(ExperimentalAnimationApi::class)
@Preview(showSystemUi = true, showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun PasswordResetScreen(
    navController: NavHostController = rememberAnimatedNavController(),
    viewModel: PasswordViewModel = viewModel(),
) {
    val context = LocalContext.current
    val passwordUiState by viewModel.passwordUiState.collectAsState()
    LaunchedEffect(key1 = passwordUiState.sendCodeTime) {
        if (!passwordUiState.sendCodeTime.isNullOrEmpty()) {
            context.getString(R.string.send_code_success).toast()
        }
    }
    LaunchedEffect(key1 = passwordUiState.pswResetSuccess) {
        if (passwordUiState.pswResetSuccess) {
            context.getString(R.string.password_reset_success).toast()
            // 返回上一个页面
            navController.navigateUp()
        }
    }
    SystemUiScaffold(statusBarsPadding = false) {
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxSize()
                .padding(20.dp)
        ) {
            AsyncImage(
                model = R.mipmap.ic_close_black,
                contentDescription = null,
                modifier = Modifier
                    .size(20.dp)
                    .clickableNoRipple {
                        navController.navigateUp()
                    },
            )
            Text(
                text = stringResource(R.string.password_reset),
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 20.dp),
            )
            AccountEditText(
                onValueChange = { viewModel.accountChanged(it) },
                text = passwordUiState.account,
                textStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold),
                hint = stringResource(R.string.account_hint),
                modifier = Modifier
                    .padding(top = 40.dp)
                    .fillMaxWidth()
                    .height(40.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next
                ),
            )
            AuthCodeEditText(
                onValueChange = { viewModel.authCodeChanged(it) },
                text = passwordUiState.authCode,
                textStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold),
                hint = stringResource(R.string.auth_code_hint),
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth()
                    .height(40.dp),
                rightTextEnabled = passwordUiState.isMobilePhone(),
                onRightTextClick = {
                    viewModel.dispatchIntent(PasswordAction.GetCode)
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
                ),
            )
            AccountEditText(
                onValueChange = { viewModel.passwordChanged(it) },
                text = passwordUiState.password,
                textStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold),
                hint = stringResource(R.string.new_password_hint),
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth()
                    .height(40.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
                ),
                visualTransformation = PasswordVisualTransformation(),
            )
            // 重置
            Button(
                onClick = {
                    viewModel.dispatchIntent(PasswordAction.Reset)
                },
                modifier = Modifier
                    .padding(top = 32.dp)
                    .fillMaxWidth()
                    .height(40.dp)
                    .alpha(if (passwordUiState.resetEnabled()) 1f else 0.6f),
                enabled = passwordUiState.resetEnabled(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Yellow30,
                    contentColor = Color.White,
                ),
            ) {
                Text(
                    text = stringResource(R.string.reset),
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
                    text = stringResource(R.string.encounter_problem),
                    color = Yellow30,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .clickableNoRipple {
                            viewModel.dispatchIntent(PasswordAction.ToggleEncounterProblemDialog)
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
                ClickableText(
                    text = buildAnnotatedString {
                        append(stringResource(R.string.login_or_register_prompt_1))
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
        EncounterProblemDialog(
            show = passwordUiState.showEncounterProblemDialog,
            type = 1,
            onForgetPassword = {
                navController.navigate(Constant.NavRoute.PASSWORD_RESET)
            },
            onContactCustomerService = {
            },
            onFeedback = {
            },
            onDismiss = {
                viewModel.dispatchIntent(PasswordAction.ToggleEncounterProblemDialog)
            },
        )
    }
}