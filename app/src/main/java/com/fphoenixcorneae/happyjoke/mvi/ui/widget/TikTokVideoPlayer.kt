package com.fphoenixcorneae.happyjoke.mvi.ui.widget

import android.net.Uri
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.fphoenixcorneae.happyjoke.R
import com.fphoenixcorneae.happyjoke.exoplayer.ExoPlayerHolder
import com.fphoenixcorneae.happyjoke.exoplayer.PlayViewMode
import com.fphoenixcorneae.happyjoke.exoplayer.PlayerViewManager
import com.fphoenixcorneae.happyjoke.ext.noRippleClickable
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player.Listener
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.util.Util
import java.lang.ref.WeakReference

/**
 * @desc：抖音视频播放器
 * @date：2023/4/15 12:15
 */
@Composable
fun TikTokVideoPlayer(
    modifier: Modifier,
    videoUrl: String?,
) {
    Box(modifier = modifier) {
        val context = LocalContext.current
        var playerView: StyledPlayerView? = null
        // 获取播放器实例
        val exoPlayer = remember { ExoPlayerHolder.get(context = context) }
        LaunchedEffect(key1 = videoUrl) {
            exoPlayer.apply {
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
                setMediaSource(mediaSource)
                // 准备播放
                prepare()
                play()
            }
        }
        AndroidView(
            factory = {
                // 创建你需要的ViewGroup 或者 View
                FrameLayout(context).apply {
                    setBackgroundColor(context.getColor(android.R.color.black))
                }
            },
            update = {
                if (PlayerViewManager.playerViewMode == PlayViewMode.HALF_SCREEN) {
                    it.removeAllViews()
                    PlayerViewManager.currentPlayerView?.get()?.player = null
                    playerView = PlayerViewManager.get(context)
                    playerView?.player = exoPlayer
                    PlayerViewManager.currentPlayerView = WeakReference(playerView)
                    playerView?.apply {
                        (parent as? ViewGroup)?.removeView(this)
                    }
                    it.addView(
                        playerView,
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                    )
                    PlayerViewManager.saveFrameLayout(it)
                }
            },
            modifier = Modifier.noRippleClickable {
                PlayerViewManager.playOrPause()
            },
        )
        TikTokPlayerController(videoUrl = videoUrl, exoPlayer = exoPlayer)
        DisposableEffect(key1 = videoUrl) {
            onDispose {
                exoPlayer.stop()
                playerView?.apply {
                    (parent as? ViewGroup)?.removeView(this)
                    PlayerViewManager.release(this)
                }
                playerView = null
            }
        }
    }
}

/**
 * @desc：
 * @date：2023/04/19 09:22
 */
@Preview
@Composable
fun TikTokPlayerController(
    videoUrl: String? = null,
    exoPlayer: ExoPlayer? = null,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        val context = LocalContext.current
        var visiblePlayIcon by remember { mutableStateOf(false) }
        // 播放按钮
        AnimatedVisibility(
            visible = visiblePlayIcon,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.Center)
                .size(48.dp),
        ) {
            Icon(
                painter = painterResource(id = R.mipmap.ic_video_play),
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.4f),
            )
        }
        var visibleBottomDivider by remember { mutableStateOf(true) }
        // 底部加载中分割线
        AnimatedVisibility(
            visible = visibleBottomDivider,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(alignment = Alignment.BottomCenter)
        ) {
            val infiniteTransition = rememberInfiniteTransition()
            val fraction by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 1500,
                        easing = CubicBezierEasing(0.2f, 0.0f, 0.4f, 1.0f)
                    ),
                    repeatMode = RepeatMode.Restart
                )
            )
            Divider(
                modifier = Modifier
                    .fillMaxWidth(fraction = fraction)
                    .alpha(alpha = 1 - fraction),
                color = Color.White,
                thickness = 2.dp
            )
        }
        DisposableEffect(key1 = videoUrl) {
            val listener = object : Listener {
                override fun onIsLoadingChanged(isLoading: Boolean) {
                    visibleBottomDivider = isLoading
                }

                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    visiblePlayIcon = !isPlaying
                }

                override fun onPlayerError(error: PlaybackException) {
                    Toast.makeText(context, "出了点小问题，请稍后重试", Toast.LENGTH_SHORT).show()
                }
            }
            exoPlayer?.addListener(listener)
            onDispose {
                exoPlayer?.removeListener(listener)
            }
        }
    }
}