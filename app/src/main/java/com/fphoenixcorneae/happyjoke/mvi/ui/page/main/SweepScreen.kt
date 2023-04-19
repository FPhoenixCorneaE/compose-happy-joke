package com.fphoenixcorneae.happyjoke.mvi.ui.page.main

import android.view.Window
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.fphoenixcorneae.happyjoke.R
import com.fphoenixcorneae.happyjoke.ext.urlAESDecrypt
import com.fphoenixcorneae.happyjoke.mvi.ui.widget.TikTokVideoPlayer
import com.fphoenixcorneae.happyjoke.mvi.ui.widget.SystemUiScaffold
import com.fphoenixcorneae.happyjoke.mvi.viewmodel.HomepageViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @desc：划一划仿抖音视频
 * @date：2023/03/17 10:46
 */
@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Preview
@Composable
fun SweepScreen(
    window: Window? = null,
    viewModel: HomepageViewModel = viewModel(),
) {
    val coroutineScope = rememberCoroutineScope()
    SystemUiScaffold(
        window = window,
        isFitsSystemWindows = false,
        isDarkFont = false,
    ) {
        val sweepTikTokVideos = viewModel.sweepTikTokVideos.collectAsLazyPagingItems()
        val pagerState = rememberPagerState()
        val currentPage by remember { derivedStateOf { pagerState.currentPage } }
        var refreshing by remember { mutableStateOf(false) }
        val pullRefreshState = rememberPullRefreshState(
            refreshing = refreshing,
            onRefresh = {
                refreshing = true
                sweepTikTokVideos.refresh()
                coroutineScope.launch {
                    delay(1000)
                    refreshing = false
                }
            },
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.onBackground)
                .padding(bottom = 50.dp)
                .pullRefresh(state = pullRefreshState)
        ) {
            VerticalPager(pageCount = sweepTikTokVideos.itemCount, state = pagerState) { page ->
                val item = sweepTikTokVideos[page]
                BoxWithConstraints {
                    if (page == currentPage) {
                        TikTokVideoPlayer(
                            modifier = Modifier.fillMaxSize(),
                            videoUrl = item?.joke?.videoUrl.urlAESDecrypt(),
                        )
                    }
                    Column(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = maxHeight * 0.3f, end = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        // 头像
                        Box(modifier = Modifier.height(56.dp)) {
                            AsyncImage(
                                model = item?.user?.avatar,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .border(width = 2.dp, color = Color.White, shape = CircleShape),
                                contentScale = ContentScale.Crop,
                            )
                            if (item?.info?.isAttention == false) {
                                AsyncImage(
                                    model = R.mipmap.ic_video_attention,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(16.dp)
                                        .align(Alignment.BottomCenter)
                                        .background(color = Color.White, shape = CircleShape)
                                )
                            }
                        }
                        // 点赞
                        AsyncImage(
                            model = R.mipmap.ic_video_like,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(top = 20.dp)
                                .size(36.dp),
                            colorFilter = ColorFilter.tint(
                                if (item?.info?.isLike == true) {
                                    Color.Red
                                } else {
                                    Color.White
                                }
                            )
                        )
                        Text(
                            text = item?.info?.likeNum.toString(),
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 14.sp,
                            ),
                            modifier = Modifier.padding(top = 8.dp),
                        )
                        // 评论
                        AsyncImage(
                            model = R.mipmap.ic_video_comment,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(top = 16.dp)
                                .size(36.dp),
                        )
                        Text(
                            text = item?.info?.likeNum.toString(),
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 14.sp,
                            ),
                            modifier = Modifier.padding(top = 8.dp),
                        )
                        // 评论
                        AsyncImage(
                            model = R.mipmap.ic_video_share,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(top = 16.dp)
                                .size(36.dp),
                        )
                        Text(
                            text = item?.info?.shareNum.toString(),
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 14.sp,
                            ),
                            modifier = Modifier.padding(top = 8.dp),
                        )
                    }
                    Column(
                        modifier = Modifier
                            .padding(20.dp)
                            .align(Alignment.BottomStart)
                    ) {
                        // 昵称
                        Text(
                            text = "@${item?.user?.nickName.orEmpty()}",
                            style = TextStyle(
                                color = Color.LightGray,
                                fontSize = 14.sp,
                            ),
                        )
                        // 视频内容
                        Text(
                            text = item?.joke?.content.orEmpty(),
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 16.sp,
                            ),
                            modifier = Modifier.padding(top = 8.dp),
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 4,
                        )
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
}