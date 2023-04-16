package com.fphoenixcorneae.happyjoke.exoplayer

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.util.Pools
import com.fphoenixcorneae.happyjoke.ext.componentActivity
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.StyledPlayerView

/**
 * @desc：用来管理 PlayerView
 * @date：2023/4/16 19:57
 */
object PlayerViewManager : ExoEventListener {

    var currentPlayerView: StyledPlayerView? = null
    var playerViewMode = PlayViewMode.HALF_SCREEN

    private val playerViewPool = Pools.SimplePool<StyledPlayerView>(2)
    private var activity: Activity? = null
    private var frameLayout: FrameLayout? = null

    fun get(context: Context): StyledPlayerView {
        return playerViewPool.acquire() ?: createPlayerView(context)
    }

    fun saveFrameLayout(frameLayout: FrameLayout) {
        this.frameLayout = frameLayout
    }

    fun release(player: StyledPlayerView) {
        playerViewPool.release(player)
    }

    /**
     * 创建PlayerView
     * */
    private fun createPlayerView(context: Context): StyledPlayerView {
        return StyledPlayerView(context).apply {
            hideController()
            useController = false
            resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    }


    /****************************************其他业务*******************************************/
    private fun switchPlayerViewMode() {
        activity = currentPlayerView?.context?.componentActivity as Activity
        if (activity?.requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            //切换竖屏
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } else {
            //切换横屏
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
    }

    fun enterFullScreen() {
        val contentRootView = activity?.findViewById<ViewGroup>(android.R.id.content)
        val params = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        )
        val parent = currentPlayerView?.parent as ViewGroup
        parent.let {
            it.removeView(currentPlayerView)
        }
        contentRootView?.addView(currentPlayerView, params)
        playerViewMode = PlayViewMode.FULL_SCREEN
    }

    fun exitFullScreen(): Boolean {
        if (isFullScreen()) {
            val contentRootView = activity?.findViewById<ViewGroup>(android.R.id.content)
            contentRootView?.let {
                // 从根View移除PlayerView
                it.removeView(currentPlayerView)
            }
            // 然后加入LazyColumn的ItemView下
            frameLayout?.addView(currentPlayerView)
            playerViewMode = PlayViewMode.HALF_SCREEN
            return true
        }
        return false
    }

    /**
     * 全屏处理
     * */
    override fun changeFullScreen(player: Player) {
        switchPlayerViewMode()
    }

    override fun backExitScreen(player: Player) {
        if (isFullScreen()) {
            exitFullScreen()
        } else {
            activity?.finish()
        }
    }

    /**
     * 暂停续播
     * */
    fun playOrPause(isPause: Boolean) {
        val player = currentPlayerView?.player
        player?.let {
            if (isPause) it.pause() else it.play()
        }
    }

    private fun isFullScreen(): Boolean = playerViewMode == PlayViewMode.FULL_SCREEN

    fun onBackPressed(): Boolean {
        return exitFullScreen()
    }
}


enum class PlayViewMode { HALF_SCREEN, FULL_SCREEN }
