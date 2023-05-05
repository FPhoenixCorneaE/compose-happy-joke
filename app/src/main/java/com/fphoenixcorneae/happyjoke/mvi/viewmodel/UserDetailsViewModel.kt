package com.fphoenixcorneae.happyjoke.mvi.viewmodel

import androidx.lifecycle.ViewModel
import com.fphoenixcorneae.happyjoke.ext.launchDefault
import com.fphoenixcorneae.happyjoke.ext.launchIo
import com.fphoenixcorneae.happyjoke.https.doOnSuccess
import com.fphoenixcorneae.happyjoke.https.httpRequest
import com.fphoenixcorneae.happyjoke.https.userService
import com.fphoenixcorneae.happyjoke.mvi.model.BaseReply
import com.fphoenixcorneae.happyjoke.mvi.model.TargetUserInfoReply
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

/**
 * @desc：
 * @date：2023/05/04 15:55
 */
class UserDetailsViewModel : ViewModel() {
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