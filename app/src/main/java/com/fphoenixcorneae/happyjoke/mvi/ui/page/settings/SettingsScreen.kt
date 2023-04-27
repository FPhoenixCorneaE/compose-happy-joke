package com.fphoenixcorneae.happyjoke.mvi.ui.page.settings

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.fphoenixcorneae.happyjoke.R
import com.fphoenixcorneae.happyjoke.ext.clickableNoRipple
import com.fphoenixcorneae.happyjoke.mvi.ui.theme.*
import com.fphoenixcorneae.happyjoke.mvi.ui.widget.SystemUiScaffold
import com.fphoenixcorneae.happyjoke.mvi.ui.widget.Toolbar
import com.fphoenixcorneae.happyjoke.tool.UserManager
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

/**
 * @desc：
 * @date：2023/4/27 20:37
 */
@OptIn(ExperimentalAnimationApi::class)
@Preview
@Composable
fun SettingsScreen(
    navController: NavHostController = rememberAnimatedNavController(),
) {
    SystemUiScaffold {
        Column {
            Toolbar(
                navController = navController,
                titleText = stringResource(R.string.settings),
            )
            Column(
                Modifier
                    .fillMaxSize()
                    .background(color = Grey15)
                    .verticalScroll(state = rememberScrollState())
            ) {
                Column(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .background(color = MaterialTheme.colorScheme.background),
                ) {
                    SettingsItem(title = "用户相关", isTitleBold = true, showRightArrow = false)
                    SettingsItem(title = "用户信息") {

                    }
                    SettingsItem(title = "账号与安全") {

                    }
                }
                Column(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .background(color = MaterialTheme.colorScheme.background),
                ) {
                    SettingsItem(title = "通用", isTitleBold = true, showRightArrow = false)
                    SettingsItem(title = "推送开关", content = "已开启") {

                    }
                    SettingsItem(title = "视频自动播放", showRightArrow = false) {

                    }
                    SettingsItem(title = "数据网络直接播放视频", showRightArrow = false) {

                    }
                    SettingsItem(title = "清除缓存", content = "34.36.KB") {

                    }
                }
                Column(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .background(color = MaterialTheme.colorScheme.background),
                ) {
                    SettingsItem(title = "其他设置", isTitleBold = true, showRightArrow = false)
                    SettingsItem(title = "给我评分") {

                    }
                    SettingsItem(title = "检查更新", content = "v3.0.2") {

                    }
                    SettingsItem(title = "用户服务协议") {

                    }
                    SettingsItem(title = "隐私政策") {

                    }
                    SettingsItem(title = "关于我们") {

                    }
                }
                // 退出登录
                if (UserManager.isLoggedIn()) {
                    Box(
                        modifier = Modifier
                            .padding(top = 12.dp, bottom = 12.dp)
                            .fillMaxWidth()
                            .height(48.dp)
                            .background(color = MaterialTheme.colorScheme.background)
                            .clickableNoRipple {
                                UserManager.logout()
                                navController.navigateUp()
                            },
                    ) {
                        Text(
                            text = "退出登录",
                            color = Yellow30,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsItem(
    title: String,
    isTitleBold: Boolean = false,
    content: String? = null,
    showRightArrow: Boolean = true,
    showDivider: Boolean = true,
    onItemClick: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clickableNoRipple {
                onItemClick()
            },
    ) {
        Text(
            text = title,
            color = Black30,
            fontSize = 14.sp,
            fontWeight = if (isTitleBold) FontWeight.Bold else null,
            modifier = Modifier
                .padding(start = 16.dp)
                .align(Alignment.CenterStart)
        )
        content?.let {
            Text(
                text = it,
                color = Black30,
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(end = 28.dp)
                    .align(Alignment.CenterEnd)
            )
        }
        if (showRightArrow) {
            Icon(
                painter = painterResource(id = R.mipmap.ic_me_arrow_right),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 12.dp)
                    .size(16.dp)
                    .align(Alignment.CenterEnd),
                tint = Grey40,
            )
        }
        if (showDivider) {
            Divider(
                thickness = 0.5.dp,
                color = GreyLine,
                modifier = Modifier.align(Alignment.BottomStart),
            )
        }
    }
}