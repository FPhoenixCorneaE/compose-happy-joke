package com.fphoenixcorneae.happyjoke.mvi.ui.page.home

import androidx.annotation.Keep
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.fphoenixcorneae.happyjoke.R
import kotlin.math.acos
import kotlin.math.sin

/**
 * @desc：底部导航栏
 * @date：2023/03/14 16:07
 */
@Preview
@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    naviBarHeight: Dp = 60.dp,
    naviItemHeight: Dp = 50.dp,
    centerIconSize: Dp = 44.dp,
    lineMarginTop: Dp = 10.dp,
    centerArcRadius: Dp = 30.dp,
) {
    val naviItems = listOf(
        NaviItem(
            name = "首页",
            icon = R.mipmap.ic_homepage_home_gray,
            selectedIcon = R.mipmap.ic_homepage_home_selected,
            selectedColor = MaterialTheme.colorScheme.onSecondary,
        ),
        NaviItem(
            name = "划一划",
            icon = R.mipmap.ic_homepage_sweep_gray,
            selectedIcon = R.mipmap.ic_homepage_sweep_selected,
            selectedColor = MaterialTheme.colorScheme.onSecondary,
        ),
        null,
        NaviItem(
            name = "消息",
            icon = R.mipmap.ic_homepage_message_gray,
            selectedIcon = R.mipmap.ic_homepage_message_selected,
            selectedColor = MaterialTheme.colorScheme.onSecondary,
        ),
        NaviItem(
            name = "我的",
            icon = R.mipmap.ic_homepage_mine_gray,
            selectedIcon = R.mipmap.ic_homepage_mine_selected,
            selectedColor = MaterialTheme.colorScheme.onSecondary,
        ),
    )
    Box(
        modifier = Modifier
            .then(modifier)
            .fillMaxWidth()
            .height(naviBarHeight)
            .drawWithContent {
                drawContent()
                val paint = Paint().apply {
                    color = Color.Gray
                    strokeWidth = 0.5f
                    style = PaintingStyle.Stroke
                }
                drawIntoCanvas { canvas ->
                    val radius = centerArcRadius.toPx()
                    val marginTop = lineMarginTop.toPx()
                    // 角度
                    val degree = Math
                        .toDegrees(acos((size.height / 2 - marginTop) / radius.toDouble()))
                        .toFloat()
                    // 对边长度
                    val oppositeLength = radius * sin(Math.toRadians(degree.toDouble())).toFloat()
                    canvas.drawLine(
                        Offset(0f, marginTop),
                        Offset(size.width / 2 - oppositeLength, marginTop),
                        paint
                    )
                    // 绘制中间圆弧
                    canvas.drawArc(
                        left = size.width / 2 - radius,
                        top = size.height / 2 - radius,
                        right = size.width / 2 + radius,
                        bottom = size.height / 2 + radius,
                        startAngle = 270f - degree,
                        sweepAngle = degree * 2,
                        useCenter = false,
                        paint = paint,
                    )
                    canvas.drawLine(
                        Offset(size.width / 2 + oppositeLength, marginTop),
                        Offset(size.width, marginTop),
                        paint
                    )
                }
            }
    ) {
        // 发布帖子
        AsyncImage(
            model = R.mipmap.ic_homepage_post_yellow,
            contentDescription = null,
            modifier = Modifier
                .size(centerIconSize)
                .align(alignment = Alignment.Center)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(naviItemHeight)
                .align(alignment = Alignment.BottomCenter),
            verticalAlignment = Alignment.CenterVertically
        ) {
            naviItems.forEachIndexed { index, naviItem ->
                naviItem?.let {
                    Column(
                        modifier = Modifier
                            .width(0.dp)
                            .weight(1f)
                            .wrapContentHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        AsyncImage(model = it.icon, contentDescription = null)
                        Text(text = it.name, style = TextStyle(fontSize = it.textSize, color = it.color))
                    }
                } ?: Box(
                    modifier = Modifier
                        .width(0.dp)
                        .weight(1f)
                        .wrapContentHeight()
                )
            }
        }
    }
}

/**
 * @desc：
 * @date：2023/03/14 16:11
 */
@Keep
data class NaviItem(
    val name: String,
    val icon: Any?,
    val selectedIcon: Any?,
    val color: Color = Color.Gray,
    val selectedColor: Color = Color.Black,
    val textSize: TextUnit = 12.sp,
)