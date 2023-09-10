package com.fphoenixcorneae.happyjoke.mvi.ui.page.user

import android.graphics.drawable.GradientDrawable
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.ColorUtils
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.fphoenixcorneae.happyjoke.R
import com.fphoenixcorneae.happyjoke.ext.*
import com.fphoenixcorneae.happyjoke.mvi.model.VideoListReply
import com.fphoenixcorneae.happyjoke.mvi.ui.page.joke.JokeItem
import com.fphoenixcorneae.happyjoke.mvi.ui.theme.*
import com.fphoenixcorneae.happyjoke.mvi.ui.widget.SystemUiScaffold
import com.fphoenixcorneae.happyjoke.mvi.ui.widget.Toolbar
import com.fphoenixcorneae.happyjoke.mvi.viewmodel.UserDetailsAction
import com.fphoenixcorneae.happyjoke.mvi.viewmodel.UserDetailsUiState
import com.fphoenixcorneae.happyjoke.mvi.viewmodel.UserDetailsViewModel
import com.fphoenixcorneae.happyjoke.mvi.viewmodel.UserDetailsViewModelFactory
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

/**
 * @desc：用户详情
 * @date：2023/05/04 15:15
 */
@OptIn(ExperimentalAnimationApi::class, ExperimentalFoundationApi::class)
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun UserDetailsScreen(
    navController: NavHostController = rememberAnimatedNavController(),
    targetUserId: String = "",
) {
    val viewModel: UserDetailsViewModel = viewModel(
        factory = UserDetailsViewModelFactory(targetUserId = targetUserId)
    )
    val userDetailsUiState by viewModel.userDetailsUiState.collectAsState()
    val labels = listOf(
        stringResource(R.string.production) to userDetailsUiState.targetUserInfo?.jokesNum.orEmpty(),
        stringResource(R.string.like) to userDetailsUiState.targetUserInfo?.jokeLikeNum.orEmpty(),
    )
    val pagerState = rememberPagerState(
        initialPage = 0, initialPageOffsetFraction = 0f
    ) {
        // provide pageCount
        labels.size
    }
    val coroutineScope = rememberCoroutineScope()
    LifecycleObserver(
        onCreate = {
            viewModel.dispatchIntent(UserDetailsAction.GetTargetUserInfo(targetUserId = targetUserId))
        },
    )
    val state = rememberCollapsingToolbarScaffoldState()
    SystemUiScaffold(
        isFitsSystemWindows = false,
        statusBarsPadding = false,
        statusBarColor = MaterialTheme.colorScheme.onSecondary.copy(alpha = 1 - state.toolbarState.progress),
        isDarkFont = false,
    ) {
        CollapsingToolbarScaffold(modifier = Modifier.fillMaxSize(),
            state = state,
            scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
            toolbar = {
                TargetUserInfo(
                    navController = navController,
                    userDetailsUiState = userDetailsUiState,
                    modifier = Modifier.graphicsLayer {
                        alpha = state.toolbarState.progress
                        translationY =
                            (260 * (1 - state.toolbarState.progress)).dp.unaryMinus().toPx()
                    },
                )
                val statusBarHeight = LocalDensity.current.run {
                    WindowInsets.statusBars.getTop(LocalDensity.current).toDp()
                }
                Column(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.onSecondary.copy(
                                alpha = 1 - state.toolbarState.progress
                            )
                        )
                        .alpha(alpha = 1 - state.toolbarState.progress)
                        .pin()
                ) {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(statusBarHeight),
                    )
                    Toolbar(
                        navController = navController,
                        background = Color.Transparent,
                        leftIconTint = Color.White,
                        rightIcon = R.mipmap.ic_more_white,
                    )
                }
                // 用户昵称
                val textSize = (17 + 3 * state.toolbarState.progress).sp
                val textColor by animateColorAsState(
                    targetValue = Color(
                        ColorUtils.blendARGB(
                            Black30.toArgb(),
                            Color.White.toArgb(),
                            1 - state.toolbarState.progress,
                        )
                    ),
                    label = "用户昵称颜色",
                )
                Text(
                    text = userDetailsUiState.targetUserInfo?.nickname.orEmpty(),
                    color = textColor,
                    fontSize = textSize,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier
                        .height(48.dp + statusBarHeight)
                        .padding(top = 16.dp + statusBarHeight)
                        .road(Alignment.Center, Alignment.TopStart)
                        .graphicsLayer {
                            translationX = (20 * state.toolbarState.progress).dp.toPx()
                            translationY = (192 * state.toolbarState.progress).dp.toPx()
                        },
                )
            }) {
            Column {
                ScrollableTabRow(selectedTabIndex = pagerState.currentPage,
                    modifier = Modifier.background(color = Color.White),
                    edgePadding = 4.dp,
                    divider = {
                        Divider(color = GreyLine, thickness = 0.5.dp)
                    },
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            modifier = Modifier.indicatorOffset(
                                tabPositions[pagerState.currentPage], 28.dp
                            ),
                            color = Yellow30,
                            height = 2.dp,
                        )
                    }) {
                    labels.forEachIndexed { index, label ->
                        Tab(
                            selected = pagerState.currentPage == index,
                            onClick = {
                                if (pagerState.currentPage != index) {
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(index)
                                    }
                                }
                            },
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = label.first, fontSize = 18.sp)
                                    Text(text = label.second, fontSize = 16.sp)
                                }
                            },
                            selectedContentColor = Yellow30,
                            unselectedContentColor = Black30,
                        )
                    }
                }
                HorizontalPager(
                    state = pagerState,
                ) { page ->
                    when (page) {
                        0 -> TargetUserJokeList(
                            navController = navController,
                            viewModel = viewModel,
                        )

                        else -> TargetUserLikeJokeList(
                            navController = navController,
                            viewModel = viewModel,
                        )
                    }
                }
            }
        }
    }
}

/**
 * @desc：用户的作品
 * @date：2023/05/06 10:29
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TargetUserJokeList(
    navController: NavHostController,
    viewModel: UserDetailsViewModel,
) {
    val labels = listOf(stringResource(R.string.text_and_pic), stringResource(R.string.video))
    val pagerState = rememberPagerState(
        initialPage = 0, initialPageOffsetFraction = 0f
    ) {
        // provide pageCount
        labels.size
    }
    val coroutineScope = rememberCoroutineScope()
    Column {
        TabRow(labels, pagerState, coroutineScope)
        Divider(color = GreyLine, thickness = 0.5.dp)
        HorizontalPager(
            state = pagerState,
        ) { page ->
            when (page) {
                0 -> TargetUserTextPicJokeList(
                    navController = navController,
                    viewModel = viewModel,
                )

                else -> TargetUserVideoJokeList(
                    navController = navController,
                    viewModel = viewModel,
                )
            }
        }
    }
}

/**
 * @desc：用户喜欢的作品
 * @date：2023/05/06 10:29
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TargetUserLikeJokeList(
    navController: NavHostController,
    viewModel: UserDetailsViewModel,
) {
    val labels = listOf(stringResource(R.string.text_and_pic), stringResource(R.string.video))
    val pagerState = rememberPagerState(
        initialPage = 0, initialPageOffsetFraction = 0f
    ) {
        // provide pageCount
        labels.size
    }
    val coroutineScope = rememberCoroutineScope()
    Column {
        TabRow(labels, pagerState, coroutineScope)
        Divider(color = GreyLine, thickness = 0.5.dp)
        HorizontalPager(
            state = pagerState,
        ) { page ->
            when (page) {
                0 -> TargetUserLikeTextPicJokeList(
                    navController = navController,
                    viewModel = viewModel,
                )

                else -> TargetUserLikeVideoJokeList(
                    navController = navController,
                    viewModel = viewModel,
                )
            }
        }
    }
}

/**
 * @desc：Tab标签
 * @date：2023/05/09 17:06
 */
@Composable
@OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
private fun TabRow(
    labels: List<String>,
    pagerState: PagerState,
    coroutineScope: CoroutineScope,
) {
    FlowRow(
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        labels.forEachIndexed { index, s ->
            Box(
                modifier = Modifier
                    .width(60.dp)
                    .height(20.dp)
                    .background(
                        color = if (pagerState.currentPage == index) Grey30.copy(alpha = 0.4f) else Color.Transparent,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .clickableNoRipple {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    }, contentAlignment = Alignment.Center
            ) {
                Text(
                    text = s,
                    fontSize = 11.sp,
                    color = Grey60,
                )
            }
        }
    }
}

/**
 * @desc：用户图文段子列表
 * @date：2023/05/06 11:21
 */
@Composable
private fun TargetUserTextPicJokeList(
    navController: NavHostController,
    viewModel: UserDetailsViewModel,
) {
    val userTextPicJokeList = viewModel.userTextPicJokeList.collectAsLazyPagingItems()
    LazyColumn {
        items(userTextPicJokeList.itemCount) {
            val isLoading = userTextPicJokeList.loadState.append is LoadState.Loading
            val item = userTextPicJokeList[it]
            JokeItem(
                navController = navController,
                joke = item,
                isLoading = isLoading,
                showUserInfo = false
            )
        }
    }
}

/**
 * @desc：用户所有视频列表
 * @date：2023/05/09 17:25
 */
@Composable
private fun TargetUserVideoJokeList(
    navController: NavHostController,
    viewModel: UserDetailsViewModel,
) {
    val userVideoJokeList = viewModel.userVideoJokeList.collectAsLazyPagingItems()
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(1.dp),
        verticalArrangement = Arrangement.spacedBy(1.dp),
    ) {
        items(items = userVideoJokeList) { item ->
            val isLoading = userVideoJokeList.loadState.append is LoadState.Loading
            VideoJokeItem(item, isLoading)
        }
    }
}

/**
 * @desc：用户喜欢的图文段子列表
 * @date：2023/05/09 17:09
 */
@Composable
private fun TargetUserLikeTextPicJokeList(
    navController: NavHostController,
    viewModel: UserDetailsViewModel,
) {
    val userLikeTextPicJokeList = viewModel.userLikeTextPicJokeList.collectAsLazyPagingItems()
    LazyColumn {
        items(userLikeTextPicJokeList.itemCount) {
            val isLoading = userLikeTextPicJokeList.loadState.append is LoadState.Loading
            val item = userLikeTextPicJokeList[it]
            JokeItem(
                navController = navController,
                joke = item,
                isLoading = isLoading,
                showUserInfo = false
            )
        }
    }
}

/**
 * @desc：用户喜欢的视频列表
 * @date：2023/05/10 10:26
 */
@Composable
private fun TargetUserLikeVideoJokeList(
    navController: NavHostController,
    viewModel: UserDetailsViewModel,
) {
    val userLikeVideoJokeList = viewModel.userLikeVideoJokeList.collectAsLazyPagingItems()
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(1.dp),
        verticalArrangement = Arrangement.spacedBy(1.dp),
    ) {
        items(items = userLikeVideoJokeList) { item ->
            val isLoading = userLikeVideoJokeList.loadState.append is LoadState.Loading
            VideoJokeItem(item, isLoading)
        }
    }
}

/**
 * @desc：
 * @date：2023/05/10 09:28
 */
@Composable
private fun VideoJokeItem(
    item: VideoListReply.Data?,
    isLoading: Boolean,
) {
    val context = LocalContext.current
    Box(
        modifier = Modifier.aspectRatio(0.75f)
    ) {
        AsyncImage(model = ImageRequest.Builder(context).data(item?.cover.urlAESDecrypt())
            .error(GradientDrawable().apply {
                setColor(Color.Black.toArgb())
            }).crossfade(true).build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .placeholder(
                    visible = isLoading,
                    color = GreyPlaceholder,
                    highlight = PlaceholderHighlight.shimmer(highlightColor = Color.White),
                )
                .clickableNoRipple {

                })
        Row(
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.BottomStart),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Image(
                painter = painterResource(id = R.mipmap.ic_like_border),
                contentDescription = null,
                modifier = Modifier.size(16.dp),
            )
            Text(
                text = item?.likeNum.orEmpty(),
                color = Color.White,
                fontSize = 14.sp,
                modifier = Modifier
                    .defaultMinSize(minWidth = 20.dp)
                    .placeholder(
                        visible = isLoading,
                        color = GreyPlaceholder,
                        shape = RoundedCornerShape(2.dp),
                        highlight = PlaceholderHighlight.shimmer(highlightColor = Color.White),
                    ),
            )
        }
    }
}

/**
 * @desc：
 * @date：2023/05/05 11:12
 */
@Composable
private fun TargetUserInfo(
    navController: NavHostController,
    userDetailsUiState: UserDetailsUiState,
    modifier: Modifier,
) {
    val context = LocalContext.current
    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
        ) {
            // 封面
            AsyncImage(
                model = ImageRequest.Builder(context).data(userDetailsUiState.targetUserInfo?.cover)
                    .crossfade(true).build(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
            // 返回Icon
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
            // 更多Icon
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
            // 头像
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(userDetailsUiState.targetUserInfo?.avatar)
                    .error(R.mipmap.ic_avatar_default).crossfade(true)
                    .transformations(CircleCropTransformation()).build(),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 20.dp)
                    .offset(y = 40.dp)
                    .size(60.dp)
                    .align(Alignment.BottomStart)
                    .border(width = 2.dp, color = Yellow30, shape = CircleShape),
            )
            // 编辑资料
            Button(
                onClick = {

                },
                modifier = Modifier
                    .padding(end = 20.dp)
                    .offset(y = 48.dp)
                    .align(alignment = Alignment.BottomEnd)
                    .width(width = 100.dp)
                    .height(height = 40.dp),
                colors = ButtonDefaults.textButtonColors(contentColor = Yellow30),
                shape = RoundedCornerShape(25.dp),
                border = BorderStroke(width = 2.dp, color = Yellow30),
                contentPadding = PaddingValues(0.dp),
            ) {
                Text(text = stringResource(R.string.edit_profile), fontSize = 14.sp)
            }
        }
        Column(
            modifier = Modifier.padding(top = 80.dp),
        ) {
            // 入驻段子乐时长
            Text(
                text = stringResource(R.string.enter_joke) + userDetailsUiState.targetUserInfo?.joinTime.orEmpty(),
                color = Grey60,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 20.dp),
            )
            // 分割线
            Divider(color = GreyLine, thickness = 0.5.dp, modifier = Modifier.padding(top = 16.dp))
            // 用户签名信息
            Text(
                text = userDetailsUiState.targetUserInfo?.sigbature.orEmpty(),
                color = Black30,
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 20.dp, top = 16.dp),
            )
            // 获赞、关注、粉丝
            TargetUserLikeAttentionFansInfo(userDetailsUiState = userDetailsUiState)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

/**
 * @desc：获赞、关注、粉丝
 * @date：2023/05/05 14:10
 */
@Composable
private fun TargetUserLikeAttentionFansInfo(userDetailsUiState: UserDetailsUiState) {
    Row(
        modifier = Modifier.padding(start = 20.dp, top = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // 获赞
        Text(
            text = userDetailsUiState.targetUserInfo?.likeNum.orEmpty(),
            color = Black30,
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold,
        )
        Text(
            text = stringResource(R.string.be_praised),
            color = Black30,
            fontSize = 15.sp,
            modifier = Modifier.padding(start = 2.dp),
        )
        // 关注
        Text(
            text = userDetailsUiState.targetUserInfo?.attentionNum.orEmpty(),
            color = Black30,
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(start = 20.dp),
        )
        Text(
            text = stringResource(id = R.string.attention),
            color = Black30,
            fontSize = 15.sp,
            modifier = Modifier.padding(start = 2.dp),
        )
        // 粉丝
        Text(
            text = userDetailsUiState.targetUserInfo?.fansNum.orEmpty(),
            color = Black30,
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(start = 20.dp),
        )
        Text(
            text = stringResource(id = R.string.fans),
            color = Black30,
            fontSize = 15.sp,
            modifier = Modifier.padding(start = 2.dp),
        )
    }
}
