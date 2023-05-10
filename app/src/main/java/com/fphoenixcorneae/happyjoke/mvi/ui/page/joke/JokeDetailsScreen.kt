package com.fphoenixcorneae.happyjoke.mvi.ui.page.joke

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.fphoenixcorneae.happyjoke.R
import com.fphoenixcorneae.happyjoke.ext.LifecycleObserver
import com.fphoenixcorneae.happyjoke.mvi.ui.widget.SystemUiScaffold
import com.fphoenixcorneae.happyjoke.mvi.ui.widget.Toolbar
import com.fphoenixcorneae.happyjoke.mvi.viewmodel.JokeDetailsAction
import com.fphoenixcorneae.happyjoke.mvi.viewmodel.JokeDetailsViewModel
import com.fphoenixcorneae.happyjoke.mvi.viewmodel.JokeDetailsViewModelFactory
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

/**
 * @desc：帖子详情
 * @date：2023/05/10 10:46
 */
@OptIn(ExperimentalAnimationApi::class)
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
    LifecycleObserver(
        onCreate = {
            viewModel.dispatchIntent(JokeDetailsAction.GetJokeDetails)
        }
    )
    SystemUiScaffold {
        Column {
            Toolbar(navController = navController, titleText = stringResource(R.string.post_details))
            Column(modifier = Modifier.verticalScroll(state = rememberScrollState())) {
                JokeDetails(
                    navController = navController,
                    joke = jokeDetailsUiState.joke,
                    isLoading = jokeDetailsUiState.joke == null,
                )
            }
        }
    }
}