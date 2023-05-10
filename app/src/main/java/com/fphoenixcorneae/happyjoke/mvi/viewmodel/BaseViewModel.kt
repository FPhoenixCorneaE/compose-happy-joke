package com.fphoenixcorneae.happyjoke.mvi.viewmodel

import androidx.lifecycle.ViewModel
import com.fphoenixcorneae.happyjoke.ext.launchDefault
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

/**
 * @desc：
 * @date：2023/05/10 11:38
 */
abstract class BaseViewModel<A> : ViewModel(), IAction<A> {

    private val action = Channel<A>()

    override fun dispatchIntent(action: A) {
        launchDefault {
            this.action.send(action)
        }
    }

    init {
        launchDefault {
            action.receiveAsFlow().collect {
                dealIntent(it)
            }
        }
    }
}

/**
 * @desc：
 * @date：2023/05/10 11:39
 */
interface IAction<A> {
    /** 分发意图 */
    fun dispatchIntent(action: A)

    /** 处理意图 */
    fun dealIntent(action: A)
}