package com.fphoenixcorneae.happyjoke.exoplayer

import com.google.android.exoplayer2.Player

/**
 * @desc：
 * @date：2023/4/16 19:44
 */
interface ExoEventListener {

    /**
     * 全屏半屏
     */
    fun changeFullScreen(player: Player)

    /**
     * 退出
     */
    fun backExitScreen(player: Player)
}