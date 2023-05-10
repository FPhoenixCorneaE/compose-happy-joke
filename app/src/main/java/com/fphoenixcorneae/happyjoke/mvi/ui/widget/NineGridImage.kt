package com.fphoenixcorneae.happyjoke.mvi.ui.widget

import android.graphics.drawable.GradientDrawable
import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.SvgDecoder
import coil.decode.VideoFrameDecoder
import coil.request.ImageRequest
import com.fphoenixcorneae.happyjoke.mvi.ui.theme.GreyPlaceholder
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer

/**
 * @desc：九宫格图片展示
 * @date：2023/04/13 14:08
 */
@Composable
fun NineGridImage(
    modifier: Modifier = Modifier,
    imageDatas: List<Any?>? = mutableListOf(),
    isLoading: Boolean = true,
    horizontalSpace: Dp = 8.dp,
    verticalSpace: Dp = 8.dp,
    cornerRadius: Dp = 8.dp,
) {
    if (imageDatas.isNullOrEmpty()) {
        return
    }
    // 将一维数组升为二维数组
    val rows = when (imageDatas.size) {
        in 1..3 -> 1
        in 4..6 -> 2
        else -> 3
    }
    val cols = 3
    val imageMatrix = Array(rows) {
        imageDatas.slice(it * cols until ((it + 1) * cols).coerceAtMost(imageDatas.size))
    }
    // 九宫格
    var size by remember { mutableStateOf(IntSize.Zero) }
    Column(
        modifier = Modifier
            .then(modifier)
            .onSizeChanged { size = it },
    ) {
        val width = LocalDensity.current.run { size.width.toDp() }
        val itemWidth = when (imageDatas.size) {
            1 -> width
            2 -> (width - verticalSpace) / 2
            else -> (width - verticalSpace * 2) / 3
        }
        if (imageDatas.size == 1) {
            Box(
                modifier = Modifier
                    .width(itemWidth)
                    .aspectRatio(16f / 9f),
            ) {
                NineGridImageItem(it = imageDatas[0], isLoading = isLoading, radius = cornerRadius)
            }
        } else {
            imageMatrix.forEachIndexed { index, rowDatas ->
                Row {
                    rowDatas.forEachIndexed { index, data ->
                        Square(
                            modifier = Modifier
                                .width(itemWidth),
                        ) {
                            NineGridImageItem(it = data, isLoading = isLoading, radius = cornerRadius)
                        }
                        if (index < rowDatas.size - 1) {
                            VerticalDivider(thickness = verticalSpace, color = Color.Transparent)
                        }
                    }
                }
                if (index < imageMatrix.size - 1) {
                    Divider(thickness = horizontalSpace, color = Color.Transparent)
                }
            }
        }
    }
}

@Composable
private fun NineGridImageItem(it: Any?, isLoading: Boolean, radius: Dp) {
    Card(shape = RoundedCornerShape(radius)) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(it)
                .error(GradientDrawable().apply {
                    shape = GradientDrawable.RECTANGLE
                    cornerRadius = LocalDensity.current.run { radius.toPx() }
                    setColor(Color.Gray.toArgb())
                })
                .crossfade(true)
                .build(),
            contentDescription = null,
            imageLoader = ImageLoader.Builder(LocalContext.current)
                .components {
                    if (SDK_INT >= 28) {
                        add(ImageDecoderDecoder.Factory())
                    } else {
                        add(GifDecoder.Factory())
                    }
                    add(SvgDecoder.Factory())
                    add(VideoFrameDecoder.Factory())
                }
                .build(),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .placeholder(
                    visible = isLoading,
                    color = GreyPlaceholder,
                    shape = RoundedCornerShape(4.dp),
                    highlight = PlaceholderHighlight.shimmer(highlightColor = Color.White),
                )
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun PreviewNineGridImageOne() {
    Box {
        NineGridImage(imageDatas = listOf(1))
    }
}