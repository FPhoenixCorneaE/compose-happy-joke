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
 * @desc：纯文
 * @date：2023/04/17 10:50
 */
@OptIn(ExperimentalAnimationApi::class)
@Preview
@Composable
fun PoliteLettersScreen(
    navController: NavHostController = rememberAnimatedNavController(),
    viewModel: HomepageViewModel = viewModel(),
) {
    val homepagePoliteLetters = viewModel.homepagePoliteLetters.collectAsLazyPagingItems()
    SwipeRefresh(
        lazyPagingItems = homepagePoliteLetters,
        contentPadding = PaddingValues(bottom = 60.dp)
    ) {
        items(homepagePoliteLetters.itemCount) {
            val isLoading = homepagePoliteLetters.loadState.append is LoadState.Loading
            val item = homepagePoliteLetters[it]
            JokeItem(navController = navController, joke = item, isLoading = isLoading)
        }
    }
}