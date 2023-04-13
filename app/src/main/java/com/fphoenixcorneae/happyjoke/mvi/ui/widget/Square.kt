package com.fphoenixcorneae.happyjoke.mvi.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp

/**
 * @desc：正方形
 * @date：2023/04/13 14:37
 */
@Composable
fun Square(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Layout(content = content, modifier = modifier) { measurables, constraints ->
        val size = constraints.maxWidth.coerceAtMost(constraints.maxHeight)
        val newConstraints = Constraints.fixed(size, size)
        val placeables = measurables.map { measurable ->
            measurable.measure(newConstraints)
        }
        layout(
            width = size,
            height = size
        ) {
            placeables.forEach { placeable ->
                placeable.placeRelative(0, 0)
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun PreviewSquareView() {
    Box(modifier = Modifier.fillMaxSize()) {
        Square(modifier = Modifier.width(100.dp)) {
            Box(
                modifier = Modifier.background(Color.Green)
            )
        }
    }
}