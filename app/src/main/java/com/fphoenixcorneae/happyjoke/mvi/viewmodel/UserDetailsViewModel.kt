package com.fphoenixcorneae.happyjoke.mvi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.fphoenixcorneae.happyjoke.ext.launchDefault
import com.fphoenixcorneae.happyjoke.ext.launchIo
import com.fphoenixcorneae.happyjoke.https.doOnSuccess
import com.fphoenixcorneae.happyjoke.https.httpRequest
import com.fphoenixcorneae.happyjoke.https.userService
import com.fphoenixcorneae.happyjoke.mvi.model.BaseReply
import com.fphoenixcorneae.happyjoke.mvi.model.TargetUserInfoReply
import com.fphoenixcorneae.happyjoke.mvi.model.paging.HomepageRecommendSource
import com.fphoenixcorneae.happyjoke.mvi.model.paging.UserTextPicJokeListSource
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

/**
 * @desc：
 * @date：2023/05/06 10:53
 */
class UserDetailsViewModelFactory(private val targetUserId: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserDetailsViewModel::class.java)) {
            return UserDetailsViewModel(targetUserId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

/**
 * @desc：
 * @date：2023/05/04 15:55
 */
class UserDetailsViewModel(private val targetUserId: String) : ViewModel() {

    /** 用户图文段子列表 */
    val userTextPicJokeList = Pager(config = PagingConfig(pageSize = 10)) {
        UserTextPicJokeListSource(targetUserId = targetUserId)
    }.flow.cachedIn(viewModelScope)

    private val _userDetailsUiState = MutableStateFlow(UserDetailsUiState())
    val userDetailsUiState = _userDetailsUiState.asStateFlow()
    private val userDetailsAction = Channel<UserDetailsAction>()

    fun dispatchIntent(action: UserDetailsAction) {
        launchDefault {
            userDetailsAction.send(action)
        }
    }

    init {
        launchDefault {
            userDetailsAction.receiveAsFlow().collect {
                when (it) {
                    is UserDetailsAction.GetTargetUserInfo -> launchIo {
                        httpRequest {
                            userService.getTargetUserInfo(targetUserId = it.targetUserId)
                        }.doOnSuccess { reply ->
                            if (reply?.code == BaseReply.OK) {
                                _userDetailsUiState.update {
                                    it.copy(targetUserInfo = reply.data)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

data class UserDetailsUiState(
    val targetUserInfo: TargetUserInfoReply.Data? = null,
)

/**
 * @desc：
 * @date：2023/05/04 16:23
 */
sealed class UserDetailsAction {
    /** 获取指定用户的信息 */
    data class GetTargetUserInfo(val targetUserId: String) : UserDetailsAction()
}