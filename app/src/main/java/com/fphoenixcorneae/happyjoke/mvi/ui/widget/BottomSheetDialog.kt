package com.fphoenixcorneae.happyjoke.mvi.ui.widget

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.fphoenixcorneae.happyjoke.ext.clickableNoRipple
import kotlin.math.roundToInt

/**
 * @desc：屏幕底部弹出弹窗，支持拖拽回弹效果，以及拖拽关闭弹窗
 * @date：2023/04/25 15:33
 */
@Composable
fun BottomSheetDialog(
    visible: Boolean,
    modifier: Modifier = Modifier,
    background: Color = Color(color = 0x4D000000),
    roundedCorners: Dp = 20.dp,
    cancelable: Boolean = true,
    canceledOnTouchOutside: Boolean = true,
    onDismiss: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    BackHandler(
        enabled = visible,
        onBack = {
            if (cancelable) {
                onDismiss()
            }
        },
    )
    Box(modifier = modifier) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(durationMillis = 400, easing = LinearEasing)),
            exit = fadeOut(animationSpec = tween(durationMillis = 400, easing = LinearEasing)),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = background)
                    .clickableNoRipple {
                        if (canceledOnTouchOutside) {
                            onDismiss()
                        }
                    }
            )
        }
        InnerDialog(
            visible = visible,
            roundedCorners = roundedCorners,
            cancelable = cancelable,
            onDismiss = onDismiss,
            content = content
        )
    }
}

@Composable
private fun BoxScope.InnerDialog(
    visible: Boolean,
    roundedCorners: Dp,
    cancelable: Boolean,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit,
) {
    var offsetY by remember { mutableStateOf(value = 0f) }
    val offsetYAnimate by animateFloatAsState(targetValue = offsetY)
    var bottomSheetHeight by remember { mutableStateOf(value = 0f) }
    AnimatedVisibility(
        modifier = Modifier
            .align(alignment = Alignment.BottomCenter)
            .clickableNoRipple {

            }
            .onGloballyPositioned {
                bottomSheetHeight = it.size.height.toFloat()
            }
            .offset(offset = {
                IntOffset(0, offsetYAnimate.roundToInt())
            })
            .draggable(
                state = rememberDraggableState(
                    onDelta = {
                        offsetY = (offsetY + it.toInt()).coerceAtLeast(0f)
                    }
                ),
                orientation = Orientation.Vertical,
                onDragStarted = {

                },
                onDragStopped = {
                    if (cancelable && offsetY > bottomSheetHeight / 2) {
                        onDismiss()
                    } else {
                        offsetY = 0f
                    }
                }
            ),
        visible = visible,
        enter = slideInVertically(
            animationSpec = tween(durationMillis = 400, easing = LinearOutSlowInEasing),
            initialOffsetY = {
                2 * it
            }
        ),
        exit = slideOutVertically(
            animationSpec = tween(durationMillis = 400, easing = LinearOutSlowInEasing),
            targetOffsetY = {
                it
            }
        )
    ) {
        DisposableEffect(key1 = null) {
            onDispose {
                offsetY = 0f
            }
        }
        Box(
            modifier = Modifier.clip(
                shape = RoundedCornerShape(
                    topStart = roundedCorners,
                    topEnd = roundedCorners,
                )
            )
        ) {
            content()
        }
    }
}