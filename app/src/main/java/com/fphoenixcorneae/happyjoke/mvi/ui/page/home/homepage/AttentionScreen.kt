package com.fphoenixcorneae.happyjoke.mvi.ui.page.home.homepage

import android.graphics.drawable.GradientDrawable
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.fphoenixcorneae.happyjoke.R
import com.fphoenixcorneae.happyjoke.ext.LifecycleObserver
import com.fphoenixcorneae.happyjoke.mvi.ui.theme.GreyBackground
import com.fphoenixcorneae.happyjoke.mvi.ui.theme.GreyPlaceholder
import com.fphoenixcorneae.happyjoke.mvi.viewmodel.HomepageViewModel
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer

/**
 * @desc：关注
 * @date：2023/04/17 10:17
 */
@Preview
@Composable
fun AttentionScreen() {
    Column(modifier = Modifier.background(color = GreyBackground)) {
        AttentionRecommend()
    }
}

/**
 * @desc：关注推荐
 * @date：2023/04/17 17:54
 */
@Preview
@Composable
fun AttentionRecommend(
    viewModel: HomepageViewModel = viewModel(),
) {
    val context = LocalContext.current
    val attentionUiState by viewModel.attentionUiState.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Text(
            text = stringResource(R.string.recommend_users),
            color = Color.DarkGray,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 20.dp, top = 20.dp),
        )
        LazyRow(modifier = Modifier.padding(top = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            item {
                Spacer(modifier = Modifier.width(12.dp))
            }
            items(attentionUiState.attentionRecommend ?: mutableListOf()) {
                Card(
                    modifier = Modifier
                        .width(150.dp)
                        .wrapContentHeight(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
                ) {
                    // 用户头像
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(it.avatar)
                            .error(GradientDrawable().apply {
                                shape = GradientDrawable.OVAL
                                setColor(Color.Gray.toArgb())
                            })
                            .transformations(CircleCropTransformation())
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .size(60.dp)
                            .align(Alignment.CenterHorizontally)
                            .border(width = 2.dp, color = MaterialTheme.colorScheme.onSecondary, shape = CircleShape)
                            .placeholder(
                                visible = it.avatar == null,
                                color = GreyPlaceholder,
                                shape = CircleShape,
                                highlight = PlaceholderHighlight.shimmer(highlightColor = Color.White),
                            )
                    )
                    // 用户昵称
                    Text(
                        text = it.nickname.orEmpty(),
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .align(Alignment.CenterHorizontally)
                            .placeholder(
                                visible = it.nickname == null,
                                color = GreyPlaceholder,
                                shape = RoundedCornerShape(4.dp),
                                highlight = PlaceholderHighlight.shimmer(highlightColor = Color.White),
                            ),
                    )
                    Row(
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        // 发表
                        Text(
                            text = stringResource(R.string.publish),
                            color = Color.Gray,
                            fontSize = 11.sp,
                            modifier = Modifier
                                .placeholder(
                                    visible = it.jokesNum == null,
                                    color = GreyPlaceholder,
                                    shape = RoundedCornerShape(4.dp),
                                    highlight = PlaceholderHighlight.shimmer(highlightColor = Color.White),
                                ),
                        )
                        Text(
                            text = it.jokesNum.orEmpty(),
                            color = Color.Gray,
                            fontSize = 11.sp,
                            modifier = Modifier
                                .padding(start = 2.dp)
                                .placeholder(
                                    visible = it.jokesNum == null,
                                    color = GreyPlaceholder,
                                    shape = RoundedCornerShape(4.dp),
                                    highlight = PlaceholderHighlight.shimmer(highlightColor = Color.White),
                                ),
                        )
                        // 粉丝
                        Text(
                            text = stringResource(R.string.fans),
                            color = Color.Gray,
                            fontSize = 11.sp,
                            modifier = Modifier
                                .padding(start = 12.dp)
                                .placeholder(
                                    visible = it.fansNum == null,
                                    color = GreyPlaceholder,
                                    shape = RoundedCornerShape(4.dp),
                                    highlight = PlaceholderHighlight.shimmer(highlightColor = Color.White),
                                ),
                        )
                        Text(
                            text = it.fansNum.orEmpty(),
                            color = Color.Gray,
                            fontSize = 11.sp,
                            modifier = Modifier
                                .padding(start = 2.dp)
                                .placeholder(
                                    visible = it.fansNum == null,
                                    color = GreyPlaceholder,
                                    shape = RoundedCornerShape(4.dp),
                                    highlight = PlaceholderHighlight.shimmer(highlightColor = Color.White),
                                ),
                        )
                    }

                    Button(
                        onClick = { },
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth()
                            .height(24.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.onSecondary,
                            contentColor = Color.White,
                        ),
                        contentPadding = PaddingValues(0.dp),
                    ) {
                        Text(
                            text = stringResource(R.string.add_attention),
                            fontSize = 14.sp,
                        )
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.width(12.dp))
            }
        }
    }
    LifecycleObserver(
        onCreate = {
            viewModel.getAttentionRecommend()
        }
    )
}