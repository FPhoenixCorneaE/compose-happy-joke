package com.fphoenixcorneae.happyjoke.mvi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.fphoenixcorneae.happyjoke.ext.launchDefault
import com.fphoenixcorneae.happyjoke.ext.launchIo
import com.fphoenixcorneae.happyjoke.https.*
import com.fphoenixcorneae.happyjoke.mvi.model.AttentionRecommendReply
import com.fphoenixcorneae.happyjoke.mvi.model.BaseReply
import com.fphoenixcorneae.happyjoke.mvi.model.UnreadMessagesReply
import com.fphoenixcorneae.happyjoke.mvi.model.paging.*
import com.fphoenixcorneae.happyjoke.tool.UserManager
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

/**
 * @desc：
 * @date：2023/03/21 17:48
 */
class HomepageViewModel : ViewModel() {

    /** 首页关注的用户发布的段子列表 */
    val homepageAttentionList = Pager(config = PagingConfig(pageSize = 10)) {
        HomepageAttentionListSource()
    }.flow.cachedIn(viewModelScope)

    /** 首页推荐列表 */
    val homepageRecommends = Pager(config = PagingConfig(pageSize = 10)) {
        HomepageRecommendSource()
    }.flow.cachedIn(viewModelScope)

    /** 首页最新列表 */
    val homepageLatests = Pager(config = PagingConfig(pageSize = 10)) {
        HomepageLatestSource()
    }.flow.cachedIn(viewModelScope)

    /** 首页纯文列表 */
    val homepagePoliteLetters = Pager(config = PagingConfig(pageSize = 10)) {
        HomepagePoliteLettersSource()
    }.flow.cachedIn(viewModelScope)

    /** 首页纯图片列表 */
    val homepageFunnyPictures = Pager(config = PagingConfig(pageSize = 10)) {
        HomepageFunnyPicturesSource()
    }.flow.cachedIn(viewModelScope)

    /** 划一划页面的推荐列表 */
    val sweepTikTokVideos = Pager(config = PagingConfig(pageSize = 10)) {
        SweepTikTokSource()
    }.flow.cachedIn(viewModelScope)

    private val _homepageUiState = MutableStateFlow(HomepageUiState())
    val homepageUiState = _homepageUiState.asStateFlow()

    private val homepageAction = Channel<HomepageAction>()

    fun dispatchIntent(action: HomepageAction) {
        launchDefault {
            homepageAction.send(action)
        }
    }

    init {
        launchDefault {
            homepageAction.receiveAsFlow().collect {
                when (it) {
                    is HomepageAction.UserAttention -> launchIo {
                        httpRequest {
                            userService.userAttention(it.status, it.userId)
                        }.doOnSuccess { reply ->
                            if (reply?.code == BaseReply.OK) {
                                // 关注/取消关注成功
                                _homepageUiState.update {
                                    it.copy(
                                        attentionResult = HomepageUiState.AttentionResult(
                                            state = reply.data?.attentionState,
                                            time = System.currentTimeMillis()
                                        )
                                    )
                                }
                            }
                        }
                    }
                    HomepageAction.GetAttentionRecommend -> launchIo {
                        httpRequest {
                            homepageService.homepageAttentionRecommend()
                        }.doOnSuccess { result ->
                            _homepageUiState.update {
                                it.copy(attentionRecommends = result?.data)
                            }
                        }
                    }
                    HomepageAction.GetUnreadMessages -> launchIo {
                        httpRequest {
                            messageService.getUnreadMessages()
                        }.doOnSuccess { result ->
                            _homepageUiState.update {
                                it.copy(unreadMessages = result?.data)
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * @desc：定义页面状态的时候，每个属性值都是不可变的，如果需要结合多个状态判断做页面展示，这种判断可直接在UiState扩展。
 * @date：2023/04/20 17:45
 */
data class HomepageUiState(
    val loginStateFlow: Flow<Boolean> = UserManager.loginStateFlow(),
    val attentionRecommends: List<AttentionRecommendReply.Data>? = null,
    val attentionResult: AttentionResult? = null,
    val unreadMessages: UnreadMessagesReply.Data? = null,
) {

    fun getMessageMark() = unreadMessages?.run {
        systemNum + likeNum + commentNum + attentionMum
    } ?: 0

    data class AttentionResult(val state: Int?, val time: Long) {
        companion object {
            const val CANCEL_ATTENTION_SUCCESS = 0
            const val ATTENTION_SUCCESS = 2
        }
    }
}

/**
 * @desc：
 * @date：2023/04/27 17:52
 */
sealed class HomepageAction {
    /** 获取推荐关注数据 */
    object GetAttentionRecommend : HomepageAction()

    /** 获取当前用户的未读消息数 */
    object GetUnreadMessages : HomepageAction()

    /** 用户关注 */
    data class UserAttention(val status: Int, val userId: String) : HomepageAction()
}
