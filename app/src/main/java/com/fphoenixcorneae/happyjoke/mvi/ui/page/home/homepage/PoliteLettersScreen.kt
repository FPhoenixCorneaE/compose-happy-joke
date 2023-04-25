package com.fphoenixcorneae.happyjoke.mvi.ui.page.home.homepage

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.fphoenixcorneae.happyjoke.mvi.ui.widget.SwipeRefresh
import com.fphoenixcorneae.happyjoke.mvi.viewmodel.HomepageViewModel

/**
 * @desc：纯文
 * @date：2023/04/17 10:50
 */
@Preview
@Composable
fun PoliteLettersScreen(
    viewModel: HomepageViewModel = viewModel(),
) {
    val homepagePoliteLetters = viewModel.homepagePoliteLetters.collectAsLazyPagingItems()
    SwipeRefresh(
        lazyPagingItems = homepagePoliteLetters,
        contentPadding = PaddingValues(bottom = 60.dp)
    ) {
        items(homepagePoliteLetters) { item ->
            val isLoading = homepagePoliteLetters.loadState.append is LoadState.Loading
            HomepageRecommendItem(item, isLoading)
        }
    }
}