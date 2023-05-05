package com.fphoenixcorneae.happyjoke.mvi.ui.page.user

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.fphoenixcorneae.happyjoke.R
import com.fphoenixcorneae.happyjoke.ext.LifecycleObserver
import com.fphoenixcorneae.happyjoke.ext.clickableNoRipple
import com.fphoenixcorneae.happyjoke.mvi.ui.theme.Grey85
import com.fphoenixcorneae.happyjoke.mvi.ui.theme.Yellow30
import com.fphoenixcorneae.happyjoke.mvi.ui.widget.SystemUiScaffold
import com.fphoenixcorneae.happyjoke.mvi.viewmodel.UserDetailsAction
import com.fphoenixcorneae.happyjoke.mvi.viewmodel.UserDetailsViewModel
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

/**
 * @desc：用户详情
 * @date：2023/05/04 15:15
 */
@OptIn(ExperimentalAnimationApi::class)
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun UserDetailsScreen(
    navController: NavHostController = rememberAnimatedNavController(),
    targetUserId: String = "",
    viewModel: UserDetailsViewModel = viewModel(),
) {
    val context = LocalContext.current
    val userDetailsUiState by viewModel.userDetailsUiState.collectAsState()
    LifecycleObserver(
        onCreate = {
            viewModel.dispatchIntent(UserDetailsAction.GetTargetUserInfo(targetUserId = targetUserId))
        },
    )
    SystemUiScaffold(isFitsSystemWindows = false, statusBarsPadding = false, isDarkFont = false) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .graphicsLayer(clip = false),
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(userDetailsUiState.targetUserInfo?.cover)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
                Image(
                    painter = painterResource(id = R.mipmap.ic_back),
                    contentDescription = null,
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(start = 20.dp, top = 8.dp)
                        .align(alignment = Alignment.TopStart)
                        .size(36.dp)
                        .background(color = Grey85.copy(alpha = 0.2f), shape = CircleShape)
                        .padding(all = 8.dp)
                        .clickableNoRipple {
                            navController.navigateUp()
                        },
                    colorFilter = ColorFilter.tint(Color.White),
                )
                Image(
                    painter = painterResource(id = R.mipmap.ic_more_white),
                    contentDescription = null,
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(end = 20.dp, top = 8.dp)
                        .align(alignment = Alignment.TopEnd)
                        .size(36.dp)
                        .background(color = Grey85.copy(alpha = 0.2f), shape = CircleShape)
                        .padding(all = 8.dp)
                        .clickableNoRipple {

                        },
                )
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(userDetailsUiState.targetUserInfo?.avatar)
                        .error(R.mipmap.ic_avatar_default)
                        .crossfade(true)
                        .transformations(CircleCropTransformation())
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .offset(y = 40.dp)
                        .size(60.dp)
                        .align(Alignment.BottomStart)
                        .border(width = 2.dp, color = Yellow30, shape = CircleShape),
                )
            }
        }
    }
}