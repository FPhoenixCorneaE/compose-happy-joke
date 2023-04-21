package com.fphoenixcorneae.happyjoke.mvi.ui.page.login

import android.view.Window
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.fphoenixcorneae.happyjoke.R
import com.fphoenixcorneae.happyjoke.ext.noRippleClickable
import com.fphoenixcorneae.happyjoke.mvi.ui.widget.AccountEditText
import com.fphoenixcorneae.happyjoke.mvi.ui.widget.AuthCodeEditText
import com.fphoenixcorneae.happyjoke.mvi.ui.widget.SystemUiScaffold
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

/**
 * @desc：登录
 * @date：2023/04/21 15:56
 */
@OptIn(ExperimentalAnimationApi::class)
@Preview
@Composable
fun LoginScreen(
    window: Window? = null,
    navController: NavHostController = rememberAnimatedNavController(),
) {
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
                    .size(24.dp)
                    .noRippleClickable {
                        navController.navigateUp()
                    },
            )
            Text(
                text = stringResource(R.string.auth_code_login),
                color = Color.Black,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(top = 20.dp),
            )
            var account by rememberSaveable { mutableStateOf("") }
            AccountEditText(
                onValueChange = { account = it },
                text = account,
                hint = "请输入手机号",
                modifier = Modifier
                    .padding(top = 40.dp)
                    .fillMaxWidth()
                    .height(40.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
            )
            var authCode by rememberSaveable { mutableStateOf("") }
            AuthCodeEditText(
                onValueChange = { authCode = it },
                text = authCode,
                hint = "请输入验证码",
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth()
                    .height(40.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
            )
        }
    }
}