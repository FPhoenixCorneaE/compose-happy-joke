package com.fphoenixcorneae.happyjoke.mvi.ui.page.main

import android.graphics.drawable.ColorDrawable
import android.view.Window
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.fphoenixcorneae.happyjoke.R
import com.fphoenixcorneae.happyjoke.ext.noRippleClickable
import com.fphoenixcorneae.happyjoke.mvi.ui.theme.GreyLine
import com.fphoenixcorneae.happyjoke.mvi.ui.widget.ShortVideoPlayer
import com.fphoenixcorneae.happyjoke.mvi.ui.widget.SystemUiScaffold
import com.fphoenixcorneae.happyjoke.mvi.viewmodel.HomepageViewModel

/**
 * @desc：首页
 * @date：2023/03/17 09:30
 */
@Preview
@Composable
fun HomepageScreen(
    window: Window? = null,
    viewModel: HomepageViewModel = viewModel(),
) {
    SystemUiScaffold(
        window = window,
        isFitsSystemWindows = true,
        statusBarColor = MaterialTheme.colorScheme.background,
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
            ) {
                var selectedPosition by rememberSaveable { mutableStateOf(1) }
                val selectedLabelColor = MaterialTheme.colorScheme.onSecondary
                Row(
                    modifier = Modifier
                        .wrapContentWidth()
                        .fillMaxHeight(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    val labels = listOf(
                        stringResource(R.string.follow),
                        stringResource(R.string.recommend),
                        stringResource(R.string.fresh),
                        stringResource(R.string.polite_letters),
                        stringResource(R.string.funny_pictures)
                    )
                    labels.forEachIndexed { index, it ->
                        val labelSize by animateFloatAsState(targetValue = if (selectedPosition == index) 18f else 16f)
                        Spacer(modifier = Modifier.width(20.dp))
                        // 标签
                        Text(
                            text = it,
                            style = TextStyle(
                                color = if (selectedPosition == index) selectedLabelColor else Color.Black,
                                fontSize = labelSize.sp
                            ),
                            fontWeight = if (selectedPosition == index) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier
                                .noRippleClickable {
                                    if (selectedPosition != index) {
                                        selectedPosition = index
                                    }
                                },
                        )
                    }
                }
                // 搜索图标
                AsyncImage(
                    model = R.mipmap.ic_search,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 20.dp)
                        .size(size = 24.dp)
                        .align(alignment = Alignment.CenterEnd),
                )
            }
            Divider(color = GreyLine, thickness = 0.5.dp)
            LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 60.dp)) {
                items(count = 10) {
                    HomepageItem()
                }
            }
        }
    }
}

/**
 * @desc：
 * @date：2023/03/17 14:25
 */
@Preview
@Composable
fun HomepageItem() {
    val context = LocalContext.current
    Column {
        Column(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                val (avatar, name, desc, more) = createRefs()
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(ColorDrawable(Color.Gray.toArgb()))
                        .transformations(CircleCropTransformation())
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .size(36.dp)
                        .constrainAs(avatar) {
                            start.linkTo(parent.start, margin = 16.dp)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }
                )
                Text(
                    text = "偷蜂蜜的跳跳虎",
                    style = TextStyle(color = Color.Black, fontSize = 14.sp),
                    modifier = Modifier.constrainAs(name) {
                        start.linkTo(avatar.end, margin = 8.dp)
                        top.linkTo(avatar.top)
                    },
                )
                Text(
                    text = "偷蜂蜜的跳跳虎",
                    style = TextStyle(color = Color.Gray, fontSize = 13.sp),
                    modifier = Modifier.constrainAs(desc) {
                        start.linkTo(name.start)
                        top.linkTo(name.bottom, margin = 2.dp)
                    },
                )
                Icon(
                    painter = painterResource(id = R.mipmap.ic_more),
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .constrainAs(more) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            end.linkTo(parent.end, margin = 16.dp)
                        },
                )
            }
            Text(
                text = "愤怒的备胎",
                style = TextStyle(color = Color.Black, fontSize = 14.sp),
                modifier = Modifier.padding(start = 16.dp, top = 8.dp),
            )
            ShortVideoPlayer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(start = 16.dp, top = 8.dp, end = 16.dp)
                    .clip(shape = RoundedCornerShape(8.dp)),
                videoUrl = "https://prod-streaming-video-msn-com.akamaized.net/a8c412fa-f696-4ff2-9c76-e8ed9cdffe0f/604a87fc-e7bc-463e-8d56-cde7e661d690.mp4"
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(id = R.mipmap.ic_like),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "1",
                        style = TextStyle(color = Color.Black, fontSize = 10.sp),
                    )
                }
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(id = R.mipmap.ic_tread),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "88",
                        style = TextStyle(color = Color.Black, fontSize = 10.sp),
                    )
                }
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(id = R.mipmap.ic_comment),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "20",
                        style = TextStyle(color = Color.Black, fontSize = 10.sp),
                    )
                }
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(id = R.mipmap.ic_share),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "38",
                        style = TextStyle(color = Color.Black, fontSize = 10.sp),
                    )
                }
            }
        }
        Divider(color = GreyLine, thickness = 10.dp)
    }
}