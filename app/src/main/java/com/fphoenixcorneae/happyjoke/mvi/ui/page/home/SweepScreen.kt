package com.fphoenixcorneae.happyjoke.mvi.ui.page.home

import android.app.Activity
import android.view.WindowManager
import androidx.compose.animation.core.*
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.fphoenixcorneae.happyjoke.R
import com.fphoenixcorneae.happyjoke.exoplayer.HttpProxyCacheManager
import com.fphoenixcorneae.happyjoke.ext.clickableNoRipple
import com.fphoenixcorneae.happyjoke.ext.urlAESDecrypt
import com.fphoenixcorneae.happyjoke.mvi.ui.widget.SystemUiScaffold
import com.fphoenixcorneae.happyjoke.mvi.ui.widget.TikTokVideoPlayer
import com.fphoenixcorneae.happyjoke.mvi.viewmodel.HomepageViewModel

/**
 * @desc：划一划仿抖音视频
 * @date：2023/03/17 10:46
 */
@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Preview
@Composable
fun SweepScreen(
    viewModel: HomepageViewModel = viewModel(),
) {
    SystemUiScaffold(statusBarsPadding = false, isDarkFont = false) {
        val context = LocalContext.current
        val sweepTikTokVideos = viewModel.sweepTikTokVideos.collectAsLazyPagingItems()
        val pagerState = rememberPagerState(
            initialPage = 0,
            initialPageOffsetFraction = 0f
        ) {
            // provide pageCount
            sweepTikTokVideos.itemCount
        }
        val currentPage by remember { derivedStateOf { pagerState.currentPage } }
        val pullRefreshState = rememberPullRefreshState(
            refreshing = sweepTikTokVideos.loadState.refresh is LoadState.Loading,
            onRefresh = { sweepTikTokVideos.refresh() },
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.onBackground)
                .padding(bottom = 50.dp)
                .pullRefresh(state = pullRefreshState)
        ) {
            VerticalPager(state = pagerState) { page ->
                val item = sweepTikTokVideos[page]
                BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                    if (page == currentPage) {
                        TikTokVideoPlayer(
                            modifier = Modifier.fillMaxSize(),
                            videoUrl = HttpProxyCacheManager.getProxyUrl(
                                context = context,
                                url = item?.joke?.videoUrl.urlAESDecrypt(),
                            ),
                        )
                    }
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(end = 8.dp, bottom = 16.dp),
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
                            model = if (item?.info?.isLike == true) R.mipmap.ic_video_like else R.mipmap.ic_video_unlike,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(top = 20.dp)
                                .size(36.dp)
                                .clickableNoRipple {

                                },
                        )
                        Text(
                            text = item?.info?.likeNum.toString(),
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 14.sp,
                            ),
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
                        )
                        // 旋转头像
                        val infiniteTransition = rememberInfiniteTransition(label = "旋转头像")
                        val rotation by infiniteTransition.animateFloat(
                            initialValue = 0f,
                            targetValue = 360f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(10000, easing = LinearEasing),
                            ),
                            label = "头像旋转角度",
                        )
                        AsyncImage(
                            model = item?.user?.avatar,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(top = 16.dp)
                                .size(48.dp)
                                .clip(CircleShape)
                                .graphicsLayer(
                                    rotationZ = rotation,
                                ),
                            contentScale = ContentScale.Crop,
                        )
                    }
                    Column(
                        modifier = Modifier
                            .padding(start = 16.dp, end = 80.dp, bottom = 16.dp)
                            .align(Alignment.BottomStart)
                    ) {
                        // 昵称
                        Text(
                            text = "@${item?.user?.nickName.orEmpty()}",
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                            ),
                        )
                        // 视频内容
                        Text(
                            text = item?.joke?.content.orEmpty(),
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 14.sp,
                            ),
                            modifier = Modifier.padding(top = 8.dp),
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 4,
                        )
                    }
                }
            }
            PullRefreshIndicator(
                refreshing = sweepTikTokVideos.loadState.refresh is LoadState.Loading,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter),
            )
        }
        DisposableEffect(Unit) {
            val activity = context as? Activity
            // 保持屏幕常亮, 无需添加任何权限
            activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            onDispose {
                // 关闭屏幕常亮
                activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
        }
    }
}