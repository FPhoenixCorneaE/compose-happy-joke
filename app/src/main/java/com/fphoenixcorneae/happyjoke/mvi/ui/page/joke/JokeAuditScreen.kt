package com.fphoenixcorneae.happyjoke.mvi.ui.page.joke

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.fphoenixcorneae.happyjoke.R
import com.fphoenixcorneae.happyjoke.mvi.ui.widget.SwipeRefresh
import com.fphoenixcorneae.happyjoke.mvi.ui.widget.SystemUiScaffold
import com.fphoenixcorneae.happyjoke.mvi.ui.widget.Toolbar
import com.fphoenixcorneae.happyjoke.mvi.viewmodel.JokeAuditViewModel
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

/**
 * @desc：审核列表
 * @date：2023/05/17 16:29
 */
@OptIn(ExperimentalAnimationApi::class)
@Preview
@Composable
fun JokeAuditScreen(
    navController: NavHostController = rememberAnimatedNavController(),
    status: String = "0",
    viewModel: JokeAuditViewModel = viewModel(),
) {
    SystemUiScaffold {
        Column(modifier = Modifier.fillMaxSize()) {
            Toolbar(
                navController = navController,
                titleText = if (status == "0") {
                    stringResource(id = R.string.auditing)
                } else {
                    stringResource(id = R.string.audit_failure)
                },
            )
            val jokeAuditList = if (status == "0") {
                viewModel.jokeAuditingList.collectAsLazyPagingItems()
            } else {
                viewModel.jokeAuditFailureList.collectAsLazyPagingItems()
            }
            SwipeRefresh(lazyPagingItems = jokeAuditList) {
                items(jokeAuditList) { item ->
                    val isLoading = jokeAuditList.loadState.append is LoadState.Loading
                    JokeItem(
                        navController = navController,
                        joke = item,
                        isLoading = isLoading,
                        showUserInfo = false,
                    )
                }
            }
        }
    }
}