package com.fphoenixcorneae.happyjoke.mvi.ui.page.joke

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.coerceAtMost
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.fphoenixcorneae.happyjoke.R
import com.fphoenixcorneae.happyjoke.const.Constant
import com.fphoenixcorneae.happyjoke.ext.LifecycleObserver
import com.fphoenixcorneae.happyjoke.ext.clickableNoRipple
import com.fphoenixcorneae.happyjoke.mvi.ui.theme.Black30
import com.fphoenixcorneae.happyjoke.mvi.ui.theme.Grey60
import com.fphoenixcorneae.happyjoke.mvi.ui.theme.GreyBackground
import com.fphoenixcorneae.happyjoke.mvi.ui.theme.GreyLine
import com.fphoenixcorneae.happyjoke.mvi.ui.theme.GreyPlaceholder
import com.fphoenixcorneae.happyjoke.mvi.ui.theme.Yellow30
import com.fphoenixcorneae.happyjoke.mvi.ui.widget.PagingLazyColumn
import com.fphoenixcorneae.happyjoke.mvi.ui.widget.SystemUiScaffold
import com.fphoenixcorneae.happyjoke.mvi.ui.widget.Toolbar
import com.fphoenixcorneae.happyjoke.mvi.viewmodel.JokeDetailsAction
import com.fphoenixcorneae.happyjoke.mvi.viewmodel.JokeDetailsViewModel
import com.fphoenixcorneae.happyjoke.mvi.viewmodel.JokeDetailsViewModelFactory
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer
import kotlinx.coroutines.launch

/**
 * @desc：帖子详情
 * @date：2023/05/10 10:46
 */
@OptIn(ExperimentalAnimationApi::class, ExperimentalFoundationApi::class)
@Preview
@Composable
fun JokeDetailsScreen(
    navController: NavHostController = rememberAnimatedNavController(),
    targetJokeId: String? = null,
) {
    val viewModel: JokeDetailsViewModel = viewModel(
        factory = JokeDetailsViewModelFactory(targetJokeId = targetJokeId.orEmpty())
    )
    val jokeDetailsUiState by viewModel.jokeDetailsUiState.collectAsState()
    val labels = listOf(
        stringResource(id = R.string.comment) to jokeDetailsUiState.joke?.info?.commentNum,
        stringResource(R.string.thumbs_up) to jokeDetailsUiState.joke?.info?.likeNum,
    )
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        // provide pageCount
        labels.size
    }
    val coroutineScope = rememberCoroutineScope()
    // 屏幕高度，不包括状态栏、导航栏高度
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    LifecycleObserver(onCreate = {
        viewModel.dispatchIntent(JokeDetailsAction.GetJokeDetails)
    })
    SystemUiScaffold {
        Column(modifier = Modifier.background(color = GreyBackground)) {
            Toolbar(
                navController = navController, titleText = stringResource(R.string.post_details)
            )
            LazyColumn(modifier = Modifier.weight(1f)) {
                item {
                    JokeContent(
                        navController = navController,
                        joke = jokeDetailsUiState.joke,
                        isLoading = jokeDetailsUiState.joke == null,
                    )
                }
                stickyHeader {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .background(color = Color.White)
                    ) {
                        val tabWidth = 72.dp
                        val indicatorWidth = 72.dp.coerceAtMost(tabWidth)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .background(color = Color.White),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            labels.forEachIndexed { index, label ->
                                Box(
                                    modifier = Modifier
                                        .widthIn(min = tabWidth)
                                        .fillMaxHeight()
                                        .clickableNoRipple {
                                            if (pagerState.currentPage != index) {
                                                coroutineScope.launch {
                                                    pagerState.animateScrollToPage(index)
                                                }
                                            }
                                        }, contentAlignment = Alignment.Center
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                                    ) {
                                        Text(
                                            text = label.first,
                                            fontSize = 15.sp,
                                            color = if (pagerState.currentPage == index) Yellow30 else Black30,
                                        )
                                        Text(
                                            text = label.second.toString(),
                                            fontSize = 13.sp,
                                            color = if (pagerState.currentPage == index) Yellow30 else Black30,
                                        )
                                    }
                                }
                            }
                        }
                        val currentTabWidth by animateDpAsState(
                            targetValue = tabWidth,
                            animationSpec = tween(
                                durationMillis = 250, easing = FastOutSlowInEasing
                            ),
                            label = "当前标签宽度",
                        )
                        val indicatorOffset by animateDpAsState(
                            targetValue = tabWidth * pagerState.currentPage,
                            animationSpec = tween(
                                durationMillis = 250, easing = FastOutSlowInEasing
                            ),
                            label = "指示器偏移量",
                        )
                        Box(
                            modifier = Modifier
                                // 水平方向上，也就是左右都有，计算出的宽度减去我们要显示的宽度，再除以2就可以就得出了两边同时要间隔的大小，此时就可以正好在中间了
                                .padding(horizontal = (currentTabWidth - indicatorWidth) / 2)
                                .width(indicatorWidth)
                                .height(3.dp)
                                .align(Alignment.BottomStart)
                                .offset(x = indicatorOffset)
                                .background(Yellow30)
                        )
                        Divider(
                            color = GreyLine,
                            thickness = 0.5.dp,
                            modifier = Modifier.align(Alignment.BottomStart)
                        )
                    }
                }
                item {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(screenHeight - (48 + 40 + 48).dp)
                    ) { page ->
                        when (page) {
                            0 -> TargetJokeCommentList(
                                navController = navController,
                                viewModel = viewModel,
                            )

                            else -> TargetJokeLikeList(
                                navController = navController,
                                viewModel = viewModel,
                            )
                        }
                    }
                }
            }
            JokeComment(navController = navController)
        }
    }
}

/**
 * @desc：评论列表
 * @date：2023/05/10 15:21
 */
@Composable
fun TargetJokeCommentList(
    navController: NavHostController,
    viewModel: JokeDetailsViewModel,
) {
    val jokeCommentList = viewModel.jokeCommentList.collectAsLazyPagingItems()
    val context = LocalContext.current
    PagingLazyColumn(lazyPagingItems = jokeCommentList) {
        items(jokeCommentList.itemCount) {
            val isLoading = jokeCommentList.loadState.append is LoadState.Loading
            val comment = jokeCommentList[it]
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 用户头像
                AsyncImage(model = ImageRequest.Builder(context)
                    .data(comment?.commentUser?.userAvatar).error(R.mipmap.ic_avatar_default)
                    .transformations(CircleCropTransformation()).crossfade(true).build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(36.dp)
                        .placeholder(
                            visible = isLoading,
                            color = GreyPlaceholder,
                            shape = CircleShape,
                            highlight = PlaceholderHighlight.shimmer(highlightColor = Color.White),
                        )
                        .clickableNoRipple {
                            navController.navigate("${Constant.NavRoute.USER_DETAILS}/${comment?.commentUser?.userId}")
                        })
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentHeight(),
                ) {
                    // 用户昵称
                    Text(
                        text = comment?.commentUser?.nickname.orEmpty(),
                        color = Grey60,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .defaultMinSize(minWidth = 32.dp)
                            .placeholder(
                                visible = isLoading,
                                color = GreyPlaceholder,
                                shape = RoundedCornerShape(4.dp),
                                highlight = PlaceholderHighlight.shimmer(highlightColor = Color.White),
                            ),
                    )
                    // 评论内容
                    Text(
                        text = comment?.content.orEmpty(),
                        color = Black30,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .defaultMinSize(minWidth = 80.dp)
                            .placeholder(
                                visible = isLoading,
                                color = GreyPlaceholder,
                                shape = RoundedCornerShape(4.dp),
                                highlight = PlaceholderHighlight.shimmer(highlightColor = Color.White),
                            ),
                    )
                }

                var isLike by rememberSaveable { mutableStateOf(comment?.isLike ?: false) }
                var likeNum by rememberSaveable { mutableStateOf(comment?.likeNum ?: 0) }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .clickableNoRipple {
                            isLike = !isLike
                            likeNum = if (isLike) {
                                likeNum + 1
                            } else {
                                (likeNum - 1).coerceAtLeast(0)
                            }
                        },
                ) {
                    Image(
                        painter = painterResource(id = if (isLike) R.mipmap.ic_comment_like else R.mipmap.ic_comment_unlike),
                        contentDescription = null,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = likeNum.toString(),
                        style = TextStyle(color = Black30, fontSize = 10.sp),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .defaultMinSize(minWidth = 16.dp)
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
    }
}

/**
 * @desc：点赞列表
 * @date：2023/05/10 15:21
 */
@Composable
fun TargetJokeLikeList(
    navController: NavHostController,
    viewModel: JokeDetailsViewModel,
) {
    val jokeLikeList = viewModel.jokeLikeList.collectAsLazyPagingItems()
    val context = LocalContext.current
    PagingLazyColumn(lazyPagingItems = jokeLikeList) {
        items(jokeLikeList.itemCount) {
            val isLoading = jokeLikeList.loadState.append is LoadState.Loading
            val like = jokeLikeList[it]
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 用户头像
                AsyncImage(model = ImageRequest.Builder(context).data(like?.avatar)
                    .error(R.mipmap.ic_avatar_default).transformations(CircleCropTransformation())
                    .crossfade(true).build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(36.dp)
                        .placeholder(
                            visible = isLoading,
                            color = GreyPlaceholder,
                            shape = CircleShape,
                            highlight = PlaceholderHighlight.shimmer(highlightColor = Color.White),
                        )
                        .clickableNoRipple {
                            navController.navigate("${Constant.NavRoute.USER_DETAILS}/${like?.userId}")
                        })
                // 用户昵称
                Text(
                    text = like?.nickname.orEmpty(),
                    color = Black30,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .defaultMinSize(minWidth = 32.dp)
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
}
