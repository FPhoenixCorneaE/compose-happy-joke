package com.fphoenixcorneae.happyjoke.mvi.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults.elevation
import androidx.compose.material.ButtonDefaults.textButtonColors
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.fphoenixcorneae.happyjoke.mvi.ui.theme.Grey30
import com.fphoenixcorneae.happyjoke.mvi.ui.theme.Grey60
import com.fphoenixcorneae.happyjoke.mvi.ui.theme.Grey70

/**
 * 下拉加载封装
 * implementation "com.google.accompanist:accompanist-swiperefresh:xxx"
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <T : Any> SwipeRefresh(
    lazyPagingItems: LazyPagingItems<T>,
    lazyListState: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    content: LazyListScope.() -> Unit,
) {
    val pullRefreshState = rememberPullRefreshState(
        refreshing = lazyPagingItems.loadState.refresh is LoadState.Loading,
        onRefresh = { lazyPagingItems.refresh() },
    )
    Box(modifier = Modifier.pullRefresh(state = pullRefreshState)) {
        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = contentPadding,
        ) {
            content()
            lazyPagingItems.apply {
                when {
                    loadState.append is LoadState.Loading -> {
                        // 加载更多，底部loading
                        item { LoadingItem() }
                    }
                    loadState.append is LoadState.Error -> {
                        // 加载更多异常
                        item {
                            ErrorMoreRetryItem {
                                lazyPagingItems.retry()
                            }
                        }
                    }
                    loadState.append == LoadState.NotLoading(endOfPaginationReached = true) -> {
                        // 没有更多数据了
                        item { NoMoreDataFindUI() }
                    }
                    loadState.refresh is LoadState.Error -> {
                        if (lazyPagingItems.itemCount <= 0) {
                            // 刷新的时候，如果itemCount小于0，第一次加载异常
                            item {
                                ErrorContent {
                                    lazyPagingItems.retry()
                                }
                            }
                        } else {
                            item {
                                ErrorMoreRetryItem {
                                    lazyPagingItems.retry()
                                }
                            }
                        }
                    }
                    loadState.refresh is LoadState.Loading -> {
                        // 第一次加载且正在加载中
                        if (lazyPagingItems.itemCount == 0) {
                        }
                    }
                }
            }

        }
        PullRefreshIndicator(
            refreshing = lazyPagingItems.loadState.refresh is LoadState.Loading,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
        )
    }
}

/**
 * 底部加载更多失败处理
 */
@Composable
fun ErrorMoreRetryItem(retry: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        TextButton(
            onClick = { retry() },
            modifier = Modifier
                .padding(20.dp)
                .width(80.dp)
                .height(30.dp),
            shape = RoundedCornerShape(6.dp),
            contentPadding = PaddingValues(3.dp),
            colors = textButtonColors(backgroundColor = Grey30),
            elevation = elevation(
                defaultElevation = 2.dp,
                pressedElevation = 4.dp,
            ),
        ) {
            Text(text = "加载更多失败，请重试！", color = Grey60)
        }
    }
}

/**
 * 底部加载更多到底了
 */
@Composable
fun NoMoreDataFindUI() {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        TextButton(
            onClick = {},
            modifier = Modifier
                .padding(20.dp)
                .width(80.dp)
                .height(30.dp),
            shape = RoundedCornerShape(6.dp),
            contentPadding = PaddingValues(3.dp),
            colors = textButtonColors(backgroundColor = Grey30),
            elevation = elevation(
                defaultElevation = 2.dp,
                pressedElevation = 4.dp,
            ),
        ) {
            Text(text = "没有更多了哟~", color = Grey60)
        }
    }
}

/**
 * 页面加载失败处理
 */
@Composable
fun ErrorContent(retry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 100.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.padding(top = 80.dp),
            painter = painterResource(id = com.fphoenixcorneae.happyjoke.R.mipmap.ic_load_error),
            contentDescription = null
        )
        Text(text = "请求失败，请检查网络！", modifier = Modifier.padding(8.dp))
        TextButton(
            onClick = { retry() },
            modifier = Modifier
                .padding(20.dp)
                .width(80.dp)
                .height(30.dp),
            shape = RoundedCornerShape(10.dp),
            contentPadding = PaddingValues(5.dp),
            colors = textButtonColors(backgroundColor = Grey30),
            elevation = elevation(
                defaultElevation = 2.dp,
                pressedElevation = 4.dp,
            )
        ) { Text(text = "重试", color = Grey70) }
    }
}

/**
 * 底部加载更多正在加载中...
 */
@Composable
fun LoadingItem() {
    Row(
        modifier = Modifier
            .height(34.dp)
            .fillMaxWidth()
            .padding(5.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(24.dp),
            color = Grey60,
            strokeWidth = 2.dp
        )
        Text(
            text = "加载中...",
            color = Grey60,
            modifier = Modifier
                .fillMaxHeight()
                .padding(start = 20.dp),
            fontSize = 18.sp,
        )
    }
}