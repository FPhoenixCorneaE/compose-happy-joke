package com.fphoenixcorneae.happyjoke.mvi.ui.widget

import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.fphoenixcorneae.happyjoke.R
import com.fphoenixcorneae.happyjoke.ext.noRippleClickable
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.util.Util
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @desc：短视频播放器
 * @date：2023/03/20 17:48
 */
@Composable
fun ShortVideoPlayer(
    modifier: Modifier,
    videoUrl: String?,
    thumbUrl: String?,
) {
    Box(modifier = modifier) {
        val context = LocalContext.current
        val scope = rememberCoroutineScope()

        val exoPlayer = remember {
            ExoPlayer.Builder(context)
                .setTrackSelector(
                    DefaultTrackSelector(context).apply {
                        setParameters(
                            buildUponParameters().setMaxVideoSizeSd()
                                .setAllowAudioMixedMimeTypeAdaptiveness(true)
                        )
                    }
                )
                .build()
                .apply {
                    videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
                    repeatMode = Player.REPEAT_MODE_ONE
                    val videoUri = Uri.parse(videoUrl.orEmpty())
                    val dataSourceFactory = DefaultDataSource.Factory(context)
                    val mediaSource = when (Util.inferContentType(videoUri)) {
                        C.CONTENT_TYPE_DASH -> DashMediaSource.Factory(dataSourceFactory)
                            .createMediaSource(MediaItem.fromUri(videoUri))
                        C.CONTENT_TYPE_HLS -> HlsMediaSource.Factory(dataSourceFactory)
                            .createMediaSource(MediaItem.fromUri(videoUri))
                        else -> ProgressiveMediaSource.Factory(dataSourceFactory)
                            .createMediaSource(MediaItem.fromUri(videoUri))
                    }
                    addMediaSource(mediaSource)
                    prepare()
                }
        }
        DisposableEffect(
            Box(modifier = Modifier.fillMaxSize()) {
                var visibleController by remember { mutableStateOf(true) }
                AndroidView(
                    factory = {
                        StyledPlayerView(context).apply {
                            hideController()
                            useController = false
                            resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                            player = exoPlayer
                            layoutParams = FrameLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                        }
                    },
                    modifier = Modifier.noRippleClickable {
                        visibleController = !visibleController
                    }
                )
                // 视频封面
                var visibleThumb by remember { mutableStateOf(true) }
                AnimatedVisibility(visible = visibleThumb) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(thumbUrl)
                            .error(ColorDrawable(Color.Black.toArgb()))
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize(),
                    )
                }
                var isPlaying by remember { mutableStateOf(exoPlayer.isPlaying) }
                AnimatedVisibility(
                    visible = visibleController,
                    enter = fadeIn(),
                    exit = fadeOut(),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(48.dp)
                        .noRippleClickable {
                            scope.launch {
                                delay(500)
                                isPlaying = if (isPlaying) {
                                    exoPlayer.pause()
                                    false
                                } else {
                                    exoPlayer.play()
                                    visibleThumb = false
                                    true
                                }
                            }
                        },
                ) {
                    Icon(
                        painter = painterResource(id = if (isPlaying) R.mipmap.ic_pause else R.mipmap.ic_play),
                        contentDescription = null,
                        tint = Color.White,
                    )
                    if (isPlaying) {
                        LaunchedEffect(Unit) {
                            delay(5000)
                            visibleController = false
                        }
                    }
                }
            }
        ) {
            onDispose {
                exoPlayer.release()
            }
        }
    }
}