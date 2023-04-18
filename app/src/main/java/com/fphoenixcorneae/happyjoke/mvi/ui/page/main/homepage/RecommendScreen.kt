package com.fphoenixcorneae.happyjoke.mvi.ui.page.main.homepage

import android.graphics.drawable.GradientDrawable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.fphoenixcorneae.happyjoke.R
import com.fphoenixcorneae.happyjoke.ext.urlAESDecrypt
import com.fphoenixcorneae.happyjoke.mvi.model.HomepageRecommend
import com.fphoenixcorneae.happyjoke.mvi.ui.theme.GreyLine
import com.fphoenixcorneae.happyjoke.mvi.ui.theme.GreyPlaceholder
import com.fphoenixcorneae.happyjoke.mvi.ui.widget.NineGridImage
import com.fphoenixcorneae.happyjoke.mvi.ui.widget.ShortVideoPlayer
import com.fphoenixcorneae.happyjoke.mvi.viewmodel.HomepageViewModel
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @desc：推荐
 * @date：2023/04/17 10:12
 */
@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun RecommendScreen(
    viewModel: HomepageViewModel = viewModel(),
) {
    val coroutineScope = rememberCoroutineScope()
    val homepageRecommends = viewModel.homepageRecommends.collectAsLazyPagingItems()
    var refreshing by remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = refreshing,
        onRefresh = {
            refreshing = true
            homepageRecommends.refresh()
            coroutineScope.launch {
                delay(1000)
                refreshing = false
            }
        },
    )
    Box(modifier = Modifier.pullRefresh(state = pullRefreshState)) {
        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 60.dp)) {
            items(homepageRecommends) { item ->
                val isLoading = homepageRecommends.loadState.append is LoadState.Loading
                HomepageRecommendItem(item, isLoading)
            }
            // Add a retry button if there was an error loading the data
            if (homepageRecommends.loadState.append is LoadState.Error) {
                item {
                    // 底部的重试按钮
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Button(
                            modifier = Modifier.align(alignment = Alignment.Center),
                            onClick = { homepageRecommends.retry() },
                        ) {
                            Text(text = "加载失败！请重试")
                        }
                    }
                }
            }
        }
        PullRefreshIndicator(
            refreshing = refreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
        )
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
                        .error(GradientDrawable().apply {
                            shape = GradientDrawable.OVAL
                            setColor(Color.Gray.toArgb())
                        })
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
                    NineGridImage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 8.dp, end = 16.dp),
                        imageDatas = homepageRecommend.joke.imageUrl?.split(",")?.mapNotNull {
                            it.urlAESDecrypt()
                        },
                        isLoading = isLoading,
                    )
                }
                (homepageRecommend?.joke?.type ?: 0) == 3 -> {
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
                        videoUrl = homepageRecommend?.joke?.videoUrl.urlAESDecrypt(),
                        thumbUrl = homepageRecommend?.joke?.thumbUrl?.urlAESDecrypt()
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