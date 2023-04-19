package com.fphoenixcorneae.happyjoke.exoplayer

import android.annotation.SuppressLint
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
import java.lang.ref.WeakReference

/**
 * @desc：用来管理 PlayerView
 * @date：2023/4/16 19:57
 */
object PlayerViewManager : ExoEventListener {

    var currentPlayerView: WeakReference<StyledPlayerView?>? = null
    var playerViewMode = PlayViewMode.HALF_SCREEN

    private val playerViewPool = Pools.SimplePool<StyledPlayerView>(2)
    private var activity: WeakReference<Activity?>? = null
    private var frameLayout: WeakReference<FrameLayout?>? = null

    fun get(context: Context): StyledPlayerView {
        return playerViewPool.acquire() ?: createPlayerView(context)
    }

    fun saveFrameLayout(frameLayout: FrameLayout) {
        this.frameLayout = WeakReference(frameLayout)
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
    @SuppressLint("SourceLockedOrientationActivity")
    private fun switchPlayerViewMode() {
        activity = WeakReference(currentPlayerView?.get()?.context?.componentActivity as? Activity)
        if (activity?.get()?.requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            // 切换竖屏
            activity?.get()?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } else {
            // 切换横屏
            activity?.get()?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
    }

    fun enterFullScreen() {
        val contentRootView = activity?.get()?.findViewById<ViewGroup>(android.R.id.content)
        val params = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        )
        val parent = currentPlayerView?.get()?.parent as? ViewGroup
        parent?.removeView(currentPlayerView?.get())
        contentRootView?.addView(currentPlayerView?.get(), params)
        playerViewMode = PlayViewMode.FULL_SCREEN
    }

    fun exitFullScreen(): Boolean {
        if (isFullScreen()) {
            val contentRootView = activity?.get()?.findViewById<ViewGroup>(android.R.id.content)
            // 从根View移除PlayerView
            contentRootView?.removeView(currentPlayerView?.get())
            // 然后加入LazyColumn的ItemView下
            frameLayout?.get()?.addView(currentPlayerView?.get())
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
            activity?.get()?.finish()
        }
    }

    /**
     * 暂停续播
     * */
    fun playOrPause() {
        val player = currentPlayerView?.get()?.player
        player?.let {
            if (it.isPlaying) it.pause() else it.play()
        }
    }

    private fun isFullScreen(): Boolean = playerViewMode == PlayViewMode.FULL_SCREEN

    fun onBackPressed(): Boolean {
        return exitFullScreen()
    }
}

enum class PlayViewMode {
    HALF_SCREEN,
    FULL_SCREEN
}
