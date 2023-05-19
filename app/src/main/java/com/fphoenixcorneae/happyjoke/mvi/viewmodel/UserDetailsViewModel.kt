package com.fphoenixcorneae.happyjoke.mvi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.fphoenixcorneae.happyjoke.ext.launchIo
import com.fphoenixcorneae.happyjoke.https.doOnSuccess
import com.fphoenixcorneae.happyjoke.https.httpRequest
import com.fphoenixcorneae.happyjoke.https.userService
import com.fphoenixcorneae.happyjoke.mvi.model.BaseReply
import com.fphoenixcorneae.happyjoke.mvi.model.TargetUserInfoReply
import com.fphoenixcorneae.happyjoke.mvi.model.paging.UserLikeTextPicJokeListSource
import com.fphoenixcorneae.happyjoke.mvi.model.paging.UserLikeVideoJokeListSource
import com.fphoenixcorneae.happyjoke.mvi.model.paging.UserTextPicJokeListSource
import com.fphoenixcorneae.happyjoke.mvi.model.paging.UserVideoJokeListSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * @desc：
 * @date：2023/05/06 10:53
 */
class UserDetailsViewModelFactory(
    private val targetUserId: String,
) : ViewModelProvider.Factory {
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
class UserDetailsViewModel(
    private val targetUserId: String,
) : BaseViewModel<UserDetailsAction>() {

    /** 用户图文段子列表 */
    val userTextPicJokeList = Pager(config = PagingConfig(pageSize = 10)) {
        UserTextPicJokeListSource(targetUserId = targetUserId)
    }.flow.cachedIn(viewModelScope)

    /** 用户所有视频列表 */
    val userVideoJokeList = Pager(config = PagingConfig(pageSize = 10)) {
        UserVideoJokeListSource(targetUserId = targetUserId)
    }.flow.cachedIn(viewModelScope)

    /** 用户喜欢的图文段子列表 */
    val userLikeTextPicJokeList = Pager(config = PagingConfig(pageSize = 10)) {
        UserLikeTextPicJokeListSource(targetUserId = targetUserId)
    }.flow.cachedIn(viewModelScope)

    /** 用户喜欢的视频列表 */
    val userLikeVideoJokeList = Pager(config = PagingConfig(pageSize = 10)) {
        UserLikeVideoJokeListSource(targetUserId = targetUserId)
    }.flow.cachedIn(viewModelScope)

    private val _userDetailsUiState = MutableStateFlow(UserDetailsUiState())
    val userDetailsUiState = _userDetailsUiState.asStateFlow()

    override fun dealIntent(action: UserDetailsAction) {
        when (action) {
            is UserDetailsAction.GetTargetUserInfo -> launchIo {
                httpRequest {
                    userService.getTargetUserInfo(targetUserId = action.targetUserId)
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