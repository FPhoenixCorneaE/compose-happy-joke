package com.fphoenixcorneae.happyjoke.mvi.ui.page.joke

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.fphoenixcorneae.happyjoke.R
import com.fphoenixcorneae.happyjoke.mvi.ui.theme.Grey18
import com.fphoenixcorneae.happyjoke.mvi.ui.theme.Grey60
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

/**
 * @desc：评论段子
 * @date：2023/05/10 16:01
 */
@OptIn(ExperimentalAnimationApi::class)
@Preview
@Composable
fun JokeComment(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberAnimatedNavController(),
) {
    Row(
        modifier = Modifier
            .then(modifier)
            .fillMaxWidth()
            .height(48.dp)
            .background(color = Color.White)
            .padding(start = 16.dp, top = 8.dp, end = 8.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(weight = 1f)
                .fillMaxHeight()
                .background(color = Grey18, shape = RoundedCornerShape(25.dp)),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(text = "留下你的精彩评论...", color = Grey60, fontSize = 14.sp, modifier = Modifier.padding(start = 16.dp))
        }
        Image(
            painter = painterResource(id = R.mipmap.ic_send_unable),
            contentDescription = null,
            modifier = Modifier
                .padding(start = 8.dp)
                .size(24.dp),
        )
    }
}