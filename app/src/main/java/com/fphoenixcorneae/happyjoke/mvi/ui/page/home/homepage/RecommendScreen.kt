package com.fphoenixcorneae.happyjoke.mvi.ui.page.home.homepage

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.fphoenixcorneae.happyjoke.mvi.ui.page.joke.JokeItem
import com.fphoenixcorneae.happyjoke.mvi.ui.widget.SwipeRefresh
import com.fphoenixcorneae.happyjoke.mvi.viewmodel.HomepageAction
import com.fphoenixcorneae.happyjoke.mvi.viewmodel.HomepageViewModel
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

/**
 * @desc：推荐
 * @date：2023/04/17 10:12
 */
@OptIn(ExperimentalAnimationApi::class)
@Preview
@Composable
fun RecommendScreen(
    navController: NavHostController = rememberAnimatedNavController(),
    viewModel: HomepageViewModel = viewModel(),
) {
    val homepageRecommends = viewModel.homepageRecommends.collectAsLazyPagingItems()
    SwipeRefresh(
        lazyPagingItems = homepageRecommends, contentPadding = PaddingValues(bottom = 60.dp)
    ) {
        items(homepageRecommends.itemCount) {
            val isLoading = homepageRecommends.loadState.append is LoadState.Loading
            val item = homepageRecommends[it]
            JokeItem(
                navController = navController,
                joke = item,
                isLoading = isLoading,
                onMoreClick = {
                    viewModel.dispatchIntent(HomepageAction.ToggleReportDialog)
                },
            )
        }
    }
}

