package com.fphoenixcorneae.happyjoke.mvi.ui.page.home

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.fphoenixcorneae.happyjoke.R
import com.fphoenixcorneae.happyjoke.const.Constant
import com.fphoenixcorneae.happyjoke.ext.clickableNoRipple
import com.fphoenixcorneae.happyjoke.mvi.ui.theme.*
import com.fphoenixcorneae.happyjoke.mvi.ui.widget.SystemUiScaffold
import com.fphoenixcorneae.happyjoke.mvi.viewmodel.MeAction
import com.fphoenixcorneae.happyjoke.mvi.viewmodel.MeViewModel
import com.fphoenixcorneae.happyjoke.tool.UserManager
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

/**
 * @desc：我的
 * @date：2023/03/17 10:48
 */
@OptIn(ExperimentalAnimationApi::class)
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun MeScreen(
    navController: NavHostController = rememberAnimatedNavController(),
    viewModel: MeViewModel = viewModel(),
) {
    val context = LocalContext.current
    val meUiState by viewModel.meUiState.collectAsState()
    val isLoggedIn by meUiState.loginStateFlow.collectAsState(initial = false)
    LaunchedEffect(key1 = isLoggedIn) {
        if (isLoggedIn) {
            viewModel.dispatchIntent(MeAction.GetUserInfo)
        }
    }
    SystemUiScaffold(statusBarColor = Grey15) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Grey15)
                .verticalScroll(state = rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .padding(all = 20.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clickableNoRipple {
                        if (UserManager.isLoggedIn()) {
                            navController.navigate("${Constant.NavRoute.USER_DETAILS}/${meUiState.userInfo?.user?.userId}")
                        } else {
                            navController.navigate(Constant.NavRoute.LOGIN)
                        }
                    },
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(meUiState.userInfo?.user?.avatar)
                        .error(R.mipmap.ic_avatar_default)
                        .crossfade(true)
                        .transformations(CircleCropTransformation())
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.dp)
                        .align(Alignment.CenterStart),
                )
                Column(
                    modifier = Modifier
                        .padding(start = 80.dp)
                        .align(Alignment.CenterStart),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = if (isLoggedIn) meUiState.userInfo?.user?.nickname.orEmpty() else stringResource(
                            R.string.login_or_register
                        ),
                        color = Black30,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = if (isLoggedIn) meUiState.userInfo?.user?.signature.orEmpty() else stringResource(
                            R.string.user_signature_hint
                        ),
                        color = Grey85,
                        fontSize = 14.sp,
                    )
                }
                AsyncImage(
                    model = R.mipmap.ic_arraw_right_grey,
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.CenterEnd),
                )
            }
            Row(
                modifier = Modifier.padding(start = 36.dp),
                horizontalArrangement = Arrangement.spacedBy(40.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (isLoggedIn) meUiState.userInfo?.info?.attentionNum.toString() else "-",
                        color = Black30,
                        fontSize = 14.sp,
                    )
                    Text(
                        text = stringResource(id = R.string.attention),
                        color = Grey60,
                        fontSize = 13.sp
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (isLoggedIn) meUiState.userInfo?.info?.fansNum.toString() else "-",
                        color = Black30,
                        fontSize = 14.sp,
                    )
                    Text(
                        text = stringResource(id = R.string.fans), color = Grey60, fontSize = 13.sp
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (isLoggedIn) meUiState.userInfo?.info?.experienceNum.toString() else "-",
                        color = Black30,
                        fontSize = 14.sp,
                    )
                    Text(text = stringResource(R.string.le_dou), color = Grey60, fontSize = 13.sp)
                }
            }
            Card(
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 16.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickableNoRipple { }) {
                        Image(
                            painter = painterResource(id = R.mipmap.ic_me_post),
                            contentDescription = null,
                            modifier = Modifier.size(36.dp),
                        )
                        Text(text = "帖子", color = Black30, fontSize = 12.sp)
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickableNoRipple { }) {
                        Image(
                            painter = painterResource(id = R.mipmap.ic_me_comment),
                            contentDescription = null,
                            modifier = Modifier.size(36.dp),
                        )
                        Text(text = "评论", color = Black30, fontSize = 12.sp)
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickableNoRipple { }) {
                        Image(
                            painter = painterResource(id = R.mipmap.ic_me_like),
                            contentDescription = null,
                            modifier = Modifier.size(36.dp),
                        )
                        Text(text = "赞过", color = Black30, fontSize = 12.sp)
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickableNoRipple { }) {
                        Image(
                            painter = painterResource(id = R.mipmap.ic_me_collect),
                            contentDescription = null,
                            modifier = Modifier.size(36.dp),
                        )
                        Text(text = "收藏", color = Black30, fontSize = 12.sp)
                    }
                }
            }
            Image(
                painter = painterResource(id = R.mipmap.ic_me_gongyue_card),
                contentDescription = null,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            Column(
                modifier = Modifier
                    .padding(start = 20.dp, top = 16.dp, end = 20.dp)
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(8.dp),
                    )
            ) {
                MeItem(iconResId = R.mipmap.ic_me_kefu, content = "我的客服")
            }
            Column(
                modifier = Modifier
                    .padding(start = 20.dp, top = 16.dp, end = 20.dp)
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(8.dp),
                    )
            ) {
                MeItem(iconResId = R.mipmap.ic_me_auditing, content = "审核中")
                MeItem(iconResId = R.mipmap.ic_me_audit_failure, content = "审核失败")
                MeItem(iconResId = R.mipmap.ic_me_audit_pending, content = "待审核")
            }
            Column(
                modifier = Modifier
                    .padding(start = 20.dp, top = 16.dp, end = 20.dp)
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(8.dp),
                    )
            ) {
                MeItem(iconResId = R.mipmap.ic_me_share, content = "分享给朋友")
                MeItem(iconResId = R.mipmap.ic_me_feedback, content = "意见反馈")
                MeItem(iconResId = R.mipmap.ic_me_praise, content = "赏个好评")
                MeItem(iconResId = R.mipmap.ic_me_setting, content = "设置") {
                    navController.navigate(Constant.NavRoute.SETTINGS)
                }
            }
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
private fun MeItem(
    iconResId: Int,
    content: String,
    onItemClick: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickableNoRipple {
                onItemClick()
            },
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = null,
            modifier = Modifier
                .size(20.dp)
                .align(Alignment.CenterStart),
        )
        Text(
            text = content,
            color = Black30,
            fontSize = 14.sp,
            modifier = Modifier
                .padding(start = 28.dp)
                .align(Alignment.CenterStart),
        )
        Icon(
            painter = painterResource(id = R.mipmap.ic_me_arrow_right),
            contentDescription = null,
            modifier = Modifier
                .size(16.dp)
                .align(Alignment.CenterEnd),
            tint = Grey40,
        )
    }
}