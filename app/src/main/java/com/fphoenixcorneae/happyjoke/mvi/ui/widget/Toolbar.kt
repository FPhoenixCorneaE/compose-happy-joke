package com.fphoenixcorneae.happyjoke.mvi.ui.widget

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.fphoenixcorneae.happyjoke.R
import com.fphoenixcorneae.happyjoke.ext.clickableNoRipple
import com.fphoenixcorneae.happyjoke.mvi.ui.theme.GreyLine
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

/**
 * @desc：工具栏
 * @date：2023/04/21 11:47
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Toolbar(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberAnimatedNavController(),
    background: Color = MaterialTheme.colorScheme.background,
    leftIconTint: Color? = null,
    titleText: String = "",
    titleTextColor: Color = Color.Black,
    rightText: String? = null,
    rightIcon: Any? = null,
    onRightTextClick: () -> Unit = {},
    onRightIconClick: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .then(modifier)
            .fillMaxWidth()
            .height(48.dp)
            .background(color = background),
    ) {
        AsyncImage(
            model = R.mipmap.ic_back,
            contentDescription = null,
            modifier = Modifier
                .padding(start = 20.dp)
                .size(24.dp)
                .align(Alignment.CenterStart)
                .clickableNoRipple {
                    navController.navigateUp()
                },
            colorFilter = leftIconTint?.run { ColorFilter.tint(this) },
        )
        Text(
            text = titleText,
            color = titleTextColor,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .widthIn(0.dp, 200.dp)
                .align(Alignment.Center),
        )
        rightText?.let {
            Text(
                text = it,
                color = Color.Black,
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(end = 20.dp)
                    .align(Alignment.CenterEnd)
                    .clickableNoRipple {
                        onRightTextClick()
                    },
            )
        }
        rightIcon?.let {
            AsyncImage(
                model = it,
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 20.dp)
                    .size(24.dp)
                    .align(Alignment.CenterEnd)
                    .clickableNoRipple {
                        onRightIconClick()
                    },
            )
        }
        Divider(
            thickness = 0.5.dp, color = GreyLine, modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun PreviewToolbar() {
    Toolbar(titleText = "标题", rightText = "测试")
}