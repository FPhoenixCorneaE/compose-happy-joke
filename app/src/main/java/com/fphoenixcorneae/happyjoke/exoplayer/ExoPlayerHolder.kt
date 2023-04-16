package com.fphoenixcorneae.happyjoke.exoplayer

import android.content.Context
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector

/**
 * @desc：播放器实例创建
 * @date：2023/4/16 20:16
 */
object ExoPlayerHolder {
    private var exoplayer: ExoPlayer? = null

    fun get(context: Context): ExoPlayer {
        if (exoplayer == null) {
            exoplayer = createExoPlayer(context)
        }
        return exoplayer!!
    }

    /**
     * 创建ExoPlayer实例
     */
    private fun createExoPlayer(context: Context): ExoPlayer {
        return ExoPlayer.Builder(context).setTrackSelector(DefaultTrackSelector(context).apply {
            setParameters(
                buildUponParameters().setMaxVideoSizeSd()
                    .setAllowAudioMixedMimeTypeAdaptiveness(true)
            )
        }).build().apply {
            videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
            // 控制 media 是否循环以及如何循环
            repeatMode = Player.REPEAT_MODE_ONE
        }
    }
}
