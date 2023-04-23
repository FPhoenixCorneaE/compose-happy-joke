package com.fphoenixcorneae.happyjoke.mvi.ui.page.main.homepage

import android.graphics.drawable.GradientDrawable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.fphoenixcorneae.happyjoke.R
import com.fphoenixcorneae.happyjoke.exoplayer.HttpProxyCacheManager
import com.fphoenixcorneae.happyjoke.ext.noRippleClickable
import com.fphoenixcorneae.happyjoke.ext.urlAESDecrypt
import com.fphoenixcorneae.happyjoke.mvi.model.HomepageRecommend
import com.fphoenixcorneae.happyjoke.mvi.ui.theme.GreyLine
import com.fphoenixcorneae.happyjoke.mvi.ui.theme.GreyPlaceholder
import com.fphoenixcorneae.happyjoke.mvi.ui.theme.Yellow30
import com.fphoenixcorneae.happyjoke.mvi.ui.widget.NineGridImage
import com.fphoenixcorneae.happyjoke.mvi.ui.widget.ShortVideoPlayer
import com.fphoenixcorneae.happyjoke.mvi.ui.widget.SwipeRefresh
import com.fphoenixcorneae.happyjoke.mvi.viewmodel.HomepageViewModel
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer

/**
 * @desc：推荐
 * @date：2023/04/17 10:12
 */
@Preview
@Composable
fun RecommendScreen(
    viewModel: HomepageViewModel = viewModel(),
) {
    val homepageRecommends = viewModel.homepageRecommends.collectAsLazyPagingItems()
    SwipeRefresh(
        lazyPagingItems = homepageRecommends,
        contentPadding = PaddingValues(bottom = 60.dp)
    ) {
        items(homepageRecommends) { item ->
            val isLoading = homepageRecommends.loadState.append is LoadState.Loading
            HomepageRecommendItem(item, isLoading)
        }
    }
}

/**
 * @desc：
 * @date：2023/03/17 14:25
 */
@Preview
@Composable
fun HomepageRecommendItem(
    homepageRecommend: HomepageRecommend.Data? = null,
    isLoading: Boolean = true,
) {
    val context = LocalContext.current
    val density = LocalDensity.current
    Column {
        Column(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                val (avatar, name, desc, attention, more) = createRefs()
                // 用户头像
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(homepageRecommend?.user?.avatar)
                        .error(GradientDrawable().apply {
                            shape = GradientDrawable.OVAL
                            setColor(Color.Gray.toArgb())
                        })
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
                        .placeholder(
                            visible = isLoading,
                            color = GreyPlaceholder,
                            shape = CircleShape,
                            highlight = PlaceholderHighlight.shimmer(highlightColor = Color.White),
                        )
                )
                // 用户昵称
                Text(
                    text = homepageRecommend?.user?.nickName.orEmpty(),
                    style = TextStyle(color = Color.Black, fontSize = 14.sp),
                    modifier = Modifier
                        .constrainAs(name) {
                            start.linkTo(avatar.end, margin = 8.dp)
                            top.linkTo(avatar.top)
                        }
                        .placeholder(
                            visible = isLoading,
                            color = GreyPlaceholder,
                            shape = RoundedCornerShape(4.dp),
                            highlight = PlaceholderHighlight.shimmer(highlightColor = Color.White),
                        ),
                )
                // 用户签名信息
                Text(
                    text = homepageRecommend?.user?.signature.orEmpty(),
                    style = TextStyle(color = Color.Gray, fontSize = 13.sp),
                    modifier = Modifier
                        .constrainAs(desc) {
                            start.linkTo(name.start)
                            top.linkTo(name.bottom, margin = 2.dp)
                        }
                        .placeholder(
                            visible = isLoading,
                            color = GreyPlaceholder,
                            shape = RoundedCornerShape(4.dp),
                            highlight = PlaceholderHighlight.shimmer(highlightColor = Color.White),
                        ),
                )
                // +关注
                Text(
                    text = stringResource(id = R.string.add_attention),
                    style = TextStyle(color = Yellow30, fontSize = 14.sp),
                    modifier = Modifier
                        .constrainAs(attention) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            end.linkTo(more.start, margin = 12.dp)
                        }
                        .placeholder(
                            visible = isLoading,
                            color = GreyPlaceholder,
                            shape = RoundedCornerShape(4.dp),
                            highlight = PlaceholderHighlight.shimmer(highlightColor = Color.White),
                        )
                        .noRippleClickable {

                        },
                )
                // 更多
                Icon(
                    painter = painterResource(id = R.mipmap.ic_more),
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .constrainAs(more) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            end.linkTo(parent.end, margin = 16.dp)
                        }
                        .noRippleClickable {

                        },
                )
            }
            // 段子内容
            Text(
                text = homepageRecommend?.joke?.content.orEmpty(),
                style = TextStyle(color = Color.Black, fontSize = 14.sp),
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .placeholder(
                        visible = isLoading,
                        color = GreyPlaceholder,
                        shape = RoundedCornerShape(4.dp),
                        highlight = PlaceholderHighlight.shimmer(highlightColor = Color.White),
                    ),
            )
            when {
                homepageRecommend?.joke?.type == 2 -> {
                    // 图片
                    NineGridImage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 8.dp, end = 16.dp),
                        imageDatas = homepageRecommend.joke.imageUrl?.split(",")?.mapNotNull {
                            it.urlAESDecrypt()
                        },
                        isLoading = isLoading,
                    )
                }
                (homepageRecommend?.joke?.type ?: 0) >= 3 -> {
                    // 视频
                    ShortVideoPlayer(
                        modifier = Modifier
                            .padding(start = 16.dp, top = 8.dp, end = 16.dp)
                            .width(density.run {
                                (homepageRecommend?.joke?.videoSize
                                    ?.split(",")
                                    ?.getOrNull(0)
                                    ?.toIntOrNull() ?: 0).toDp()
                            })
                            .aspectRatio(
                                homepageRecommend?.joke?.videoSize
                                    ?.split(",")
                                    ?.run {
                                        (getOrNull(0)?.toIntOrNull() ?: 1) /
                                                (getOrNull(1)?.toIntOrNull() ?: 1).toFloat()
                                    } ?: 1f
                            )
                            .clip(shape = RoundedCornerShape(8.dp))
                            .placeholder(
                                visible = isLoading,
                                color = GreyPlaceholder,
                                shape = RoundedCornerShape(4.dp),
                                highlight = PlaceholderHighlight.shimmer(highlightColor = Color.White),
                            ),
                        videoUrl = HttpProxyCacheManager.getProxyUrl(
                            context = context,
                            url = homepageRecommend?.joke?.videoUrl.urlAESDecrypt(),
                        ),
                        thumbUrl = homepageRecommend?.joke?.thumbUrl?.urlAESDecrypt()
                    )
                }
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
            )
            Divider(color = GreyLine, thickness = 0.5.dp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 点赞数
                Row(
                    modifier = Modifier
                        .weight(1f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(id = R.mipmap.ic_like),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = (homepageRecommend?.info?.likeNum ?: 0).toString(),
                        style = TextStyle(color = Color.Black, fontSize = 10.sp),
                        modifier = Modifier
                            .defaultMinSize(minWidth = 30.dp)
                            .placeholder(
                                visible = isLoading,
                                color = GreyPlaceholder,
                                shape = RoundedCornerShape(4.dp),
                                highlight = PlaceholderHighlight.shimmer(highlightColor = Color.White),
                            ),
                    )
                }
                // 踩的数量
                Row(
                    modifier = Modifier
                        .weight(1f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(id = R.mipmap.ic_tread),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = (homepageRecommend?.info?.disLikeNum ?: 0).toString(),
                        style = TextStyle(color = Color.Black, fontSize = 10.sp),
                        modifier = Modifier
                            .defaultMinSize(minWidth = 30.dp)
                            .placeholder(
                                visible = isLoading,
                                color = GreyPlaceholder,
                                shape = RoundedCornerShape(4.dp),
                                highlight = PlaceholderHighlight.shimmer(highlightColor = Color.White),
                            ),
                    )
                }
                // 评论数
                Row(
                    modifier = Modifier
                        .weight(1f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(id = R.mipmap.ic_comment),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = (homepageRecommend?.info?.commentNum ?: 0).toString(),
                        style = TextStyle(color = Color.Black, fontSize = 10.sp),
                        modifier = Modifier
                            .defaultMinSize(minWidth = 30.dp)
                            .placeholder(
                                visible = isLoading,
                                color = GreyPlaceholder,
                                shape = RoundedCornerShape(4.dp),
                                highlight = PlaceholderHighlight.shimmer(highlightColor = Color.White),
                            ),
                    )
                }
                // 分享数
                Row(
                    modifier = Modifier
                        .weight(1f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(id = R.mipmap.ic_share),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = (homepageRecommend?.info?.shareNum ?: 0).toString(),
                        style = TextStyle(color = Color.Black, fontSize = 10.sp),
                        modifier = Modifier
                            .defaultMinSize(minWidth = 30.dp)
                            .placeholder(
                                visible = isLoading,
                                color = GreyPlaceholder,
                                shape = RoundedCornerShape(4.dp),
                                highlight = PlaceholderHighlight.shimmer(highlightColor = Color.White),
                            ),
                    )
                }
            }
        }
        Divider(color = GreyLine, thickness = 10.dp)
    }
}