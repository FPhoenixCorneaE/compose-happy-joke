package com.fphoenixcorneae.happyjoke.mvi.ui.page.home

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.fphoenixcorneae.happyjoke.R
import com.fphoenixcorneae.happyjoke.ext.clickableNoRipple
import com.fphoenixcorneae.happyjoke.mvi.ui.page.home.homepage.*
import com.fphoenixcorneae.happyjoke.mvi.ui.theme.GreyLine
import com.fphoenixcorneae.happyjoke.mvi.ui.widget.SystemUiScaffold
import com.fphoenixcorneae.happyjoke.mvi.viewmodel.HomepageViewModel
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import kotlinx.coroutines.launch

/**
 * @desc：首页，默认推荐
 * @date：2023/03/17 09:30
 */
@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
@Preview
@Composable
fun HomepageScreen(
    navController: NavHostController = rememberAnimatedNavController(),
    viewModel: HomepageViewModel = viewModel(),
) {
    SystemUiScaffold {
        val labels = listOf(
            stringResource(R.string.attention),
            stringResource(R.string.recommend),
            stringResource(R.string.latest),
            stringResource(R.string.polite_letters),
            stringResource(R.string.funny_pictures)
        )
        val pagerState = rememberPagerState(
            initialPage = 1,
            initialPageOffsetFraction = 0f
        ) {
            // provide pageCount
            labels.size
        }
        val coroutineScope = rememberCoroutineScope()
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(color = MaterialTheme.colorScheme.background)
                    .zIndex(1f),
            ) {
                Row(
                    modifier = Modifier
                        .wrapContentWidth()
                        .fillMaxHeight(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    labels.forEachIndexed { index, label ->
                        val selectedPosition = pagerState.currentPage
                        val labelSize by animateFloatAsState(
                            targetValue = if (selectedPosition == index) 18f else 16f,
                            label = "标签大小",
                        )
                        val color by animateColorAsState(
                            targetValue = if (selectedPosition == index) MaterialTheme.colorScheme.onSecondary else Color.Black,
                            label = "标签颜色",
                        )
                        // 标签
                        Text(
                            text = label,
                            color = color,
                            fontSize = labelSize.sp,
                            fontWeight = if (selectedPosition == index) FontWeight.Bold else FontWeight.Normal,
                            textAlign = TextAlign.End,
                            modifier = Modifier
                                .widthIn(min = 50.dp)
                                .clickableNoRipple {
                                    if (selectedPosition != index) {
                                        coroutineScope.launch {
                                            pagerState.animateScrollToPage(index)
                                        }
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
                        .align(alignment = Alignment.CenterEnd)
                        .clickableNoRipple {

                        },
                )
            }
            Divider(color = GreyLine, thickness = 0.5.dp)
            HorizontalPager(state = pagerState) { page ->
                when (page) {
                    0 -> AttentionScreen(navController = navController)
                    1 -> RecommendScreen(navController = navController, viewModel = viewModel)
                    2 -> LatestScreen(navController = navController)
                    3 -> PoliteLettersScreen(navController = navController)
                    else -> FunnyPicturesScreen(navController = navController)
                }
            }
        }
    }
}

