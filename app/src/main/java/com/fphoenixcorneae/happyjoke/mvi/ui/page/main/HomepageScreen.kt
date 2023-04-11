package com.fphoenixcorneae.happyjoke.mvi.ui.page.main

import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Window
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import com.fphoenixcorneae.happyjoke.R
import com.fphoenixcorneae.happyjoke.ext.noRippleClickable
import com.fphoenixcorneae.happyjoke.ext.urlDESDecrypt
import com.fphoenixcorneae.happyjoke.mvi.model.HomepageRecommend
import com.fphoenixcorneae.happyjoke.mvi.ui.theme.GreyLine
import com.fphoenixcorneae.happyjoke.mvi.ui.theme.GreyPlaceholder
import com.fphoenixcorneae.happyjoke.mvi.ui.widget.ShortVideoPlayer
import com.fphoenixcorneae.happyjoke.mvi.ui.widget.SystemUiScaffold
import com.fphoenixcorneae.happyjoke.mvi.viewmodel.HomepageViewModel
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer

/**
 * @desc：首页
 * @date：2023/03/17 09:30
 */
@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun HomepageScreen(
    window: Window? = null,
    viewModel: HomepageViewModel = viewModel(),
) {
    SystemUiScaffold(
        window = window,
        isFitsSystemWindows = true,
        statusBarColor = MaterialTheme.colorScheme.background,
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(color = MaterialTheme.colorScheme.background)
                    .zIndex(1f),
            ) {
                var selectedPosition by rememberSaveable { mutableStateOf(1) }
                val selectedLabelColor = MaterialTheme.colorScheme.onSecondary
                Row(
                    modifier = Modifier
                        .wrapContentWidth()
                        .fillMaxHeight(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    val labels = listOf(
                        stringResource(R.string.follow),
                        stringResource(R.string.recommend),
                        stringResource(R.string.fresh),
                        stringResource(R.string.polite_letters),
                        stringResource(R.string.funny_pictures)
                    )
                    labels.forEachIndexed { index, it ->
                        val labelSize by animateFloatAsState(targetValue = if (selectedPosition == index) 18f else 16f)
                        Spacer(modifier = Modifier.width(20.dp))
                        // 标签
                        Text(
                            text = it,
                            style = TextStyle(
                                color = if (selectedPosition == index) selectedLabelColor else Color.Black,
                                fontSize = labelSize.sp
                            ),
                            fontWeight = if (selectedPosition == index) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier
                                .noRippleClickable {
                                    if (selectedPosition != index) {
                                        selectedPosition = index
                                    }
                                },
                        )
                    }
                }
                // 搜索图标
                AsyncImage(
                    model = R.mipmap.ic_search,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 20.dp)
                        .size(size = 24.dp)
                        .align(alignment = Alignment.CenterEnd),
                )
            }
            Divider(color = GreyLine, thickness = 0.5.dp)
            val homepageState by viewModel.state.collectAsState()
            val pullRefreshState = rememberPullRefreshState(
                refreshing = homepageState.isRefreshing,
                onRefresh = { viewModel.refresh() },
            )
            Box(modifier = Modifier.pullRefresh(state = pullRefreshState)) {
                LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 60.dp)) {
                    items(count = homepageState.homepageRecommend?.data?.size ?: 0) {
                        val homepageRecommend = homepageState.homepageRecommend?.data?.getOrNull(it)
                        HomepageRecommendItem(homepageRecommend, homepageState.isLoading)
                    }
                }

                PullRefreshIndicator(
                    refreshing = homepageState.isRefreshing,
                    state = pullRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter),
                )
            }
        }
    }
}

/**
 * @desc：
 * @date：2023/03/17 14:25
 */
@Preview
@Composable
fun HomepageRecommendItem(
    homepageRecommend: HomepageRecommend.Data? = null,
    isLoading: Boolean = true,
) {
    val context = LocalContext.current
    val density = LocalDensity.current
    Column {
        Column(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                val (avatar, name, desc, more) = createRefs()
                // 用户头像
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(homepageRecommend?.user?.avatar)
                        .error(ColorDrawable(Color.Gray.toArgb()))
                        .transformations(CircleCropTransformation())
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .size(36.dp)
                        .constrainAs(avatar) {
                            start.linkTo(parent.start, margin = 16.dp)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }
                        .placeholder(
                            visible = isLoading,
                            color = GreyPlaceholder,
                            shape = CircleShape,
                            highlight = PlaceholderHighlight.shimmer(highlightColor = Color.White),
                        )
                )
                // 用户昵称
                Text(
                    text = homepageRecommend?.user?.nickName.orEmpty(),
                    style = TextStyle(color = Color.Black, fontSize = 14.sp),
                    modifier = Modifier
                        .constrainAs(name) {
                            start.linkTo(avatar.end, margin = 8.dp)
                            top.linkTo(avatar.top)
                        }
                        .placeholder(
                            visible = isLoading,
                            color = GreyPlaceholder,
                            shape = RoundedCornerShape(4.dp),
                            highlight = PlaceholderHighlight.shimmer(highlightColor = Color.White),
                        ),
                )
                // 用户签名信息
                Text(
                    text = homepageRecommend?.user?.signature.orEmpty(),
                    style = TextStyle(color = Color.Gray, fontSize = 13.sp),
                    modifier = Modifier
                        .constrainAs(desc) {
                            start.linkTo(name.start)
                            top.linkTo(name.bottom, margin = 2.dp)
                        }
                        .placeholder(
                            visible = isLoading,
                            color = GreyPlaceholder,
                            shape = RoundedCornerShape(4.dp),
                            highlight = PlaceholderHighlight.shimmer(highlightColor = Color.White),
                        ),
                )
                // 更多
                Icon(
                    painter = painterResource(id = R.mipmap.ic_more),
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .constrainAs(more) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            end.linkTo(parent.end, margin = 16.dp)
                        },
                )
            }
            // 段子内容
            Text(
                text = homepageRecommend?.joke?.content.orEmpty(),
                style = TextStyle(color = Color.Black, fontSize = 14.sp),
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .placeholder(
                        visible = isLoading,
                        color = GreyPlaceholder,
                        shape = RoundedCornerShape(4.dp),
                        highlight = PlaceholderHighlight.shimmer(highlightColor = Color.White),
                    ),
            )
            when {
                homepageRecommend?.joke?.type == 2 -> {
                    // 图片
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(
                                homepageRecommend.joke.imageUrl?.split(",")?.filter { it.isNotBlank() }?.first()
                                    .urlDESDecrypt().also {
                                        Log.d("段子乐", "解码后的图片地址：$it")
                                    })
                            .error(ColorDrawable(Color.Gray.toArgb()))
                            .transformations(RoundedCornersTransformation(density.run { 8.dp.toPx() }))
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        modifier = Modifier
                            .width(density.run {
                                (homepageRecommend.joke.imageSize
                                    ?.split(",")
                                    ?.getOrNull(0)
                                    ?.split("x")
                                    ?.getOrNull(0)
                                    ?.toIntOrNull() ?: 0).toDp()
                            })
                            .height(density.run {
                                (homepageRecommend.joke.imageSize
                                    ?.split(",")
                                    ?.getOrNull(0)
                                    ?.split("x")
                                    ?.getOrNull(1)
                                    ?.toIntOrNull() ?: 0).toDp()
                            })
                            .padding(start = 16.dp, top = 8.dp, end = 16.dp)
                            .placeholder(
                                visible = isLoading,
                                color = GreyPlaceholder,
                                shape = RoundedCornerShape(4.dp),
                                highlight = PlaceholderHighlight.shimmer(highlightColor = Color.White),
                            ),
                    )
                }
                (homepageRecommend?.joke?.type ?: 0) >= 3 -> {
                    // 视频
                    ShortVideoPlayer(
                        modifier = Modifier
                            .width(density.run {
                                (homepageRecommend?.joke?.videoSize
                                    ?.split(",")
                                    ?.getOrNull(0)
                                    ?.toIntOrNull() ?: 0).toDp()
                            })
                            .height(density.run {
                                (homepageRecommend?.joke?.videoSize
                                    ?.split(",")
                                    ?.getOrNull(1)
                                    ?.toIntOrNull() ?: 0).toDp()
                            })
                            .padding(start = 16.dp, top = 8.dp, end = 16.dp)
                            .clip(shape = RoundedCornerShape(8.dp))
                            .placeholder(
                                visible = isLoading,
                                color = GreyPlaceholder,
                                shape = RoundedCornerShape(4.dp),
                                highlight = PlaceholderHighlight.shimmer(highlightColor = Color.White),
                            ),
                        videoUrl = homepageRecommend?.joke?.videoUrl.urlDESDecrypt().also {
                            Log.d("段子乐", "解码后的视频地址：$it")
                        },
                        thumbUrl = homepageRecommend?.joke?.thumbUrl?.urlDESDecrypt().also {
                            Log.d("段子乐", "解码后的视频封面地址：$it")
                        }
                    )
                }
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
            )
            Divider(color = GreyLine, thickness = 0.5.dp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 点赞数
                Row(
                    modifier = Modifier
                        .weight(1f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(id = R.mipmap.ic_like),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = (homepageRecommend?.info?.likeNum ?: 0).toString(),
                        style = TextStyle(color = Color.Black, fontSize = 10.sp),
                        modifier = Modifier
                            .defaultMinSize(minWidth = 30.dp)
                            .placeholder(
                                visible = isLoading,
                                color = GreyPlaceholder,
                                shape = RoundedCornerShape(4.dp),
                                highlight = PlaceholderHighlight.shimmer(highlightColor = Color.White),
                            ),
                    )
                }
                // 踩的数量
                Row(
                    modifier = Modifier
                        .weight(1f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(id = R.mipmap.ic_tread),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = (homepageRecommend?.info?.disLikeNum ?: 0).toString(),
                        style = TextStyle(color = Color.Black, fontSize = 10.sp),
                        modifier = Modifier
                            .defaultMinSize(minWidth = 30.dp)
                            .placeholder(
                                visible = isLoading,
                                color = GreyPlaceholder,
                                shape = RoundedCornerShape(4.dp),
                                highlight = PlaceholderHighlight.shimmer(highlightColor = Color.White),
                            ),
                    )
                }
                // 评论数
                Row(
                    modifier = Modifier
                        .weight(1f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(id = R.mipmap.ic_comment),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = (homepageRecommend?.info?.commentNum ?: 0).toString(),
                        style = TextStyle(color = Color.Black, fontSize = 10.sp),
                        modifier = Modifier
                            .defaultMinSize(minWidth = 30.dp)
                            .placeholder(
                                visible = isLoading,
                                color = GreyPlaceholder,
                                shape = RoundedCornerShape(4.dp),
                                highlight = PlaceholderHighlight.shimmer(highlightColor = Color.White),
                            ),
                    )
                }
                // 分享数
                Row(
                    modifier = Modifier
                        .weight(1f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(id = R.mipmap.ic_share),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = (homepageRecommend?.info?.shareNum ?: 0).toString(),
                        style = TextStyle(color = Color.Black, fontSize = 10.sp),
                        modifier = Modifier
                            .defaultMinSize(minWidth = 30.dp)
                            .placeholder(
                                visible = isLoading,
                                color = GreyPlaceholder,
                                shape = RoundedCornerShape(4.dp),
                                highlight = PlaceholderHighlight.shimmer(highlightColor = Color.White),
                            ),
                    )
                }
            }
        }
        Divider(color = GreyLine, thickness = 10.dp)
    }
}