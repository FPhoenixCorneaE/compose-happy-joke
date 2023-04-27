package com.fphoenixcorneae.happyjoke.mvi.ui.page.joke

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.fphoenixcorneae.happyjoke.R
import com.fphoenixcorneae.happyjoke.ext.clickableNoRipple
import com.fphoenixcorneae.happyjoke.mvi.ui.theme.Grey60
import com.fphoenixcorneae.happyjoke.mvi.ui.widget.SystemUiScaffold
import com.fphoenixcorneae.happyjoke.mvi.ui.widget.Toolbar
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

/**
 * @desc：发布段子
 * @date：2023/04/20 15:10
 */
@OptIn(ExperimentalAnimationApi::class)
@Preview
@Composable
fun JokePostScreen(
    navController: NavHostController = rememberAnimatedNavController(),
) {
    SystemUiScaffold {
        Column(Modifier.fillMaxWidth()) {
            Toolbar(
                navController = navController,
                titleText = stringResource(R.string.joke_post),
                rightText = stringResource(R.string.post),
            )
            var content by rememberSaveable { mutableStateOf("") }
            val contentMaxLengths = 300
            BasicTextField(
                value = content,
                onValueChange = { content = it },
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
                    .height(200.dp),
                textStyle = TextStyle(color = Color.Black, fontSize = 14.sp),
            ) { innerTextField ->
                if (content.isEmpty()) {
                    Text(text = stringResource(R.string.joke_post_hint), color = Grey60, fontSize = 14.sp)
                }
                // 原本输入框的内容
                innerTextField()
            }
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
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
                Text(
                    text = "${content.length} / ${contentMaxLengths}字",
                    color = Grey60,
                    fontSize = 12.sp,
                    modifier = Modifier.constrainAs(contentLimit) {
                        end.linkTo(parent.end, margin = 20.dp)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                )
            }
        }
    }
}