package com.fphoenixcorneae.happyjoke.mvi.ui.page.home.homepage

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.fphoenixcorneae.happyjoke.mvi.ui.page.joke.JokeItem
import com.fphoenixcorneae.happyjoke.mvi.ui.widget.SwipeRefresh
import com.fphoenixcorneae.happyjoke.mvi.viewmodel.HomepageViewModel
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

/**
 * @desc：新鲜
 * @date：2023/04/17 10:50
 */
@OptIn(ExperimentalAnimationApi::class)
@Preview
@Composable
fun LatestScreen(
    navController: NavHostController = rememberAnimatedNavController(),
    viewModel: HomepageViewModel = viewModel(),
) {
    val homepageLatests = viewModel.homepageLatests.collectAsLazyPagingItems()
    SwipeRefresh(
        lazyPagingItems = homepageLatests,
        contentPadding = PaddingValues(bottom = 60.dp)
    ) {
        items(homepageLatests.itemCount) {
            val isLoading = homepageLatests.loadState.append is LoadState.Loading
            val item = homepageLatests[it]
            JokeItem(navController = navController, joke = item, isLoading = isLoading)
        }
    }
}