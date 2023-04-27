package com.fphoenixcorneae.happyjoke.mvi.ui.widget

import androidx.annotation.Keep
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.fphoenixcorneae.happyjoke.R
import com.fphoenixcorneae.happyjoke.ext.clickableNoRipple
import com.fphoenixcorneae.happyjoke.mvi.ui.theme.Yellow40
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
    currentPosition: Int = 0,
    naviBarHeight: Dp = 60.dp,
    naviItemHeight: Dp = 50.dp,
    centerIconSize: Dp = 44.dp,
    lineMarginTop: Dp = 10.dp,
    centerArcRadius: Dp = 30.dp,
    darkMode: Boolean = false,
    onCenterIconClick: () -> Unit = {},
    onNavItemClick: (Int) -> Unit = {},
) {
    val naviItems = listOf(
        NaviItem(
            name = stringResource(R.string.homepage),
            icon = R.mipmap.ic_homepage_home_gray,
            selectedIcon = R.mipmap.ic_homepage_home_selected,
            selectedColor = Yellow40,
        ),
        NaviItem(
            name = stringResource(R.string.sweep),
            icon = R.mipmap.ic_homepage_sweep_gray,
            selectedIcon = R.mipmap.ic_homepage_sweep_selected,
            selectedColor = MaterialTheme.colorScheme.surface,
        ),
        null,
        NaviItem(
            name = stringResource(R.string.message),
            icon = R.mipmap.ic_homepage_message_gray,
            selectedIcon = R.mipmap.ic_homepage_message_selected,
            selectedColor = Yellow40,
        ),
        NaviItem(
            name = stringResource(R.string.me),
            icon = R.mipmap.ic_homepage_me_gray,
            selectedIcon = R.mipmap.ic_homepage_me_selected,
            selectedColor = Yellow40,
        ),
    )
    var isDarkMode by remember { mutableStateOf(darkMode) }
    Box(modifier = Modifier
        .then(modifier)
        .fillMaxWidth()
        .height(naviBarHeight)
        .drawWithContent {
            if (!isDarkMode) {
                drawIntoCanvas { canvas ->
                    canvas.drawCircle(
                        Offset(size.width / 2, size.height / 2),
                        centerArcRadius.toPx(),
                        Paint().apply {
                            color = Color.White
                        },
                    )
                }
            }
            drawContent()
            if (isDarkMode) {
                return@drawWithContent
            }
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
                    Offset(0f, marginTop), Offset(size.width / 2 - oppositeLength, marginTop), paint
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
        }) {
        val backgroundColor = remember { Animatable(Color.White) }
        LaunchedEffect(key1 = isDarkMode) {
            backgroundColor.animateTo(
                targetValue = if (isDarkMode) Color.Black else Color.White,
                animationSpec = tween(200),
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(naviItemHeight)
                .align(alignment = Alignment.BottomCenter)
                .background(color = backgroundColor.value, shape = RoundedCornerShape(2.dp)),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val coroutineScope = rememberCoroutineScope()
            var selectedPosition by remember { mutableStateOf(currentPosition) }
            // 创建当前动画状态
            var animState by remember { mutableStateOf(AnimState.Static) }
            // 创建Transition，管理状态
            val transition = updateTransition(targetState = animState, label = "iconTransition")
            // 根据transition来创建动画值
            val rotate by transition.animateFloat(label = "rotate") { state ->
                when (state) {
                    AnimState.Static -> 0f
                    AnimState.Start -> 45f
                    AnimState.Reverse -> 0f
                }
            }
            // 根据transition来创建动画值
            val scale by transition.animateFloat(label = "scale") { state ->
                when (state) {
                    AnimState.Static -> 1f
                    AnimState.Start -> 1.2f
                    AnimState.Reverse -> 1f
                }
            }
            naviItems.forEachIndexed { index, naviItem ->
                naviItem?.let {
                    Column(
                        modifier = Modifier
                            .width(0.dp)
                            .weight(1f)
                            .wrapContentHeight()
                            .clickableNoRipple {
                                if (selectedPosition != index) {
                                    selectedPosition = index
                                    isDarkMode = index == 1
                                    coroutineScope.launch {
                                        animState = AnimState.Start
                                        delay(200)
                                        animState = AnimState.Reverse
                                    }
                                    onNavItemClick(index)
                                }
                            },
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        AsyncImage(
                            model = if (selectedPosition == index) it.selectedIcon else it.icon,
                            contentDescription = null,
                            colorFilter = if (isDarkMode) ColorFilter.tint(color = Color.White) else null,
                            modifier = Modifier
                                .rotate(if (selectedPosition == index) rotate else 0f)
                                .scale(if (selectedPosition == index) scale else 1f)
                        )
                        Text(
                            text = it.name,
                            style = TextStyle(
                                color = if (selectedPosition == index) it.selectedColor else it.color,
                                fontSize = it.textSize,
                                fontWeight = if (selectedPosition == index) FontWeight.Bold else null,
                            ),
                        )
                    }
                } ?: Box(
                    modifier = Modifier
                        .width(0.dp)
                        .weight(1f)
                        .wrapContentHeight()
                )
            }
        }
        // 发布帖子
        AsyncImage(
            model = R.mipmap.ic_homepage_post_yellow,
            contentDescription = null,
            modifier = Modifier
                .size(centerIconSize)
                .align(alignment = Alignment.Center)
                .clickableNoRipple {
                    onCenterIconClick()
                },
            colorFilter = if (isDarkMode) ColorFilter.tint(color = Color.White) else null,
        )
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

/**
 * @desc：
 * @date：2023/03/16 17:28
 */
enum class AnimState {
    Static, Start, Reverse,
}