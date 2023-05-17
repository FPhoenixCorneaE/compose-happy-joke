package com.fphoenixcorneae.happyjoke.mvi.ui.page.joke

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.fphoenixcorneae.happyjoke.R
import com.fphoenixcorneae.happyjoke.ext.appPackageName
import com.fphoenixcorneae.happyjoke.ext.clickableNoRipple
import com.fphoenixcorneae.happyjoke.ext.toast
import com.fphoenixcorneae.happyjoke.mvi.ui.theme.Grey60
import com.fphoenixcorneae.happyjoke.mvi.ui.widget.SystemUiScaffold
import com.fphoenixcorneae.happyjoke.mvi.ui.widget.Toolbar
import com.fphoenixcorneae.happyjoke.mvi.viewmodel.JokePostAction
import com.fphoenixcorneae.happyjoke.mvi.viewmodel.JokePostViewModel
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import github.leavesczy.matisse.Matisse
import github.leavesczy.matisse.MatisseContract
import github.leavesczy.matisse.SmartCaptureStrategy
import kotlinx.coroutines.launch

/**
 * @desc：发布段子
 * @date：2023/04/20 15:10
 */
@OptIn(ExperimentalAnimationApi::class)
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun JokePostScreen(
    navController: NavHostController = rememberAnimatedNavController(),
    viewModel: JokePostViewModel = viewModel(),
) {
    val coroutineScope = rememberCoroutineScope()
    val jokePostUiState by viewModel.jokePostUiState.collectAsState()
    LaunchedEffect(key1 = jokePostUiState.postResultMsg) {
        jokePostUiState.postResultMsg?.toast()
    }
    LaunchedEffect(key1 = jokePostUiState.postSuccess) {
        if (jokePostUiState.postSuccess) {
            navController.navigateUp()
        }
    }
    SystemUiScaffold {
        Column(Modifier.fillMaxSize()) {
            Toolbar(
                navController = navController,
                titleText = stringResource(R.string.joke_post),
                rightText = stringResource(R.string.post),
                onRightTextClick = {
                    coroutineScope.launch {
                        if (jokePostUiState.isContentBlank()) {
                            "帖子内容不能为空！".toast()
                            return@launch
                        }
                        if (jokePostUiState.isContentExceedLimit()) {
                            "帖子内容长度超出限制！".toast()
                            return@launch
                        }
                        viewModel.dispatchIntent(JokePostAction.PostJoke)
                    }
                }
            )
            val contentMaxLengths = 300
            BasicTextField(
                value = jokePostUiState.jokePostParams.content,
                onValueChange = { viewModel.contentChanged(content = it) },
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
                    .height(200.dp),
                textStyle = TextStyle(color = Color.Black, fontSize = 14.sp),
            ) { innerTextField ->
                if (jokePostUiState.jokePostParams.content.isEmpty()) {
                    Text(
                        text = stringResource(R.string.joke_post_hint),
                        color = Grey60,
                        fontSize = 14.sp
                    )
                }
                // 原本输入框的内容
                innerTextField()
            }
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                // 图片选择
                val imagePickerLauncher = rememberLauncherForActivityResult(contract = MatisseContract()) { result ->
                    Log.i("JokePostScreen", "JokePostScreen: $result")
                    if (result.isNotEmpty()) {
                        val mediaResource = result[0]
                        val imageUri = mediaResource.uri
                        val imagePath = mediaResource.path
                        val imageWidth = mediaResource.width
                        val imageHeight = mediaResource.height
                    }
                }
                val (pic, video, contentLimit) = createRefs()
                AsyncImage(
                    model = R.mipmap.ic_pic,
                    contentDescription = null,
                    modifier = Modifier
                        .size(36.dp)
                        .constrainAs(pic) {
                            start.linkTo(parent.start, margin = 20.dp)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }
                        .clickableNoRipple {
                            imagePickerLauncher.launch(
                                Matisse(
                                    maxSelectable = 9,
                                    supportedMimeTypes = Matisse.ofImage(hasGif = true),
                                    captureStrategy = SmartCaptureStrategy("$appPackageName.image-picker"),
                                )
                            )
                        },
                )
                AsyncImage(
                    model = R.mipmap.ic_video,
                    contentDescription = null,
                    modifier = Modifier
                        .size(36.dp)
                        .constrainAs(video) {
                            start.linkTo(pic.end, margin = 36.dp)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }
                        .clickableNoRipple {

                        },
                )
                Text(text = "${jokePostUiState.jokePostParams.content.length} / ${contentMaxLengths}字",
                    color = Grey60,
                    fontSize = 12.sp,
                    modifier = Modifier.constrainAs(contentLimit) {
                        end.linkTo(parent.end, margin = 20.dp)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    })
            }
            LazyVerticalGrid(columns = GridCells.Fixed(3)) {

            }
        }
    }
}