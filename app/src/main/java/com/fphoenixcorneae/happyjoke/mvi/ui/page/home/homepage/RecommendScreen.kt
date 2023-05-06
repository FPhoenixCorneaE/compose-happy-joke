package com.fphoenixcorneae.happyjoke.mvi.ui.page.home.homepage

import android.graphics.drawable.GradientDrawable
import androidx.compose.animation.ExperimentalAnimationApi
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.fphoenixcorneae.happyjoke.R
import com.fphoenixcorneae.happyjoke.const.Constant
import com.fphoenixcorneae.happyjoke.exoplayer.HttpProxyCacheManager
import com.fphoenixcorneae.happyjoke.ext.clickableNoRipple
import com.fphoenixcorneae.happyjoke.ext.urlAESDecrypt
import com.fphoenixcorneae.happyjoke.mvi.model.JokeListReply
import com.fphoenixcorneae.happyjoke.mvi.ui.page.joke.JokeItem
import com.fphoenixcorneae.happyjoke.mvi.ui.theme.GreyLine
import com.fphoenixcorneae.happyjoke.mvi.ui.theme.GreyPlaceholder
import com.fphoenixcorneae.happyjoke.mvi.ui.theme.Yellow30
import com.fphoenixcorneae.happyjoke.mvi.ui.widget.NineGridImage
import com.fphoenixcorneae.happyjoke.mvi.ui.widget.ShortVideoPlayer
import com.fphoenixcorneae.happyjoke.mvi.ui.widget.SwipeRefresh
import com.fphoenixcorneae.happyjoke.mvi.viewmodel.HomepageViewModel
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer

/**
 * @desc：推荐
 * @date：2023/04/17 10:12
 */
@OptIn(ExperimentalAnimationApi::class)
@Preview
@Composable
fun RecommendScreen(
    navController: NavHostController = rememberAnimatedNavController(),
    viewModel: HomepageViewModel = viewModel(),
) {
    val homepageRecommends = viewModel.homepageRecommends.collectAsLazyPagingItems()
    SwipeRefresh(
        lazyPagingItems = homepageRecommends,
        contentPadding = PaddingValues(bottom = 60.dp)
    ) {
        items(homepageRecommends) { item ->
            val isLoading = homepageRecommends.loadState.append is LoadState.Loading
            JokeItem(navController = navController, joke = item, isLoading = isLoading)
        }
    }
}

