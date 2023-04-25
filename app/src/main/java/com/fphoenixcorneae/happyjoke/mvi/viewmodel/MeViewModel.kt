package com.fphoenixcorneae.happyjoke.mvi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fphoenixcorneae.happyjoke.ext.launchDefault
import com.fphoenixcorneae.happyjoke.ext.launchIo
import com.fphoenixcorneae.happyjoke.https.doOnSuccess
import com.fphoenixcorneae.happyjoke.https.httpRequest
import com.fphoenixcorneae.happyjoke.https.userService
import com.fphoenixcorneae.happyjoke.mvi.model.BaseReply
import com.fphoenixcorneae.happyjoke.mvi.model.UserInfoReply
import com.fphoenixcorneae.happyjoke.tool.UserManager
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * @desc：
 * @date：2023/04/24 17:00
 */
class MeViewModel : ViewModel() {
    private val _meUiState = MutableStateFlow(MeUiState())
    val meUiState = _meUiState.asStateFlow()
    private val meAction = Channel<MeAction>()

    fun dispatchIntent(action: MeAction) {
        launchDefault {
            meAction.send(action)
        }
    }

    init {
        viewModelScope.launch {
            meAction.receiveAsFlow().collect {
                when (it) {
                    MeAction.GetUserInfo -> launchIo {
                        httpRequest {
                            userService.getUserInfo()
                        }.doOnSuccess { reply ->
                            if (reply?.code == BaseReply.OK) {
                                reply.data.let { userInfo ->
                                    _meUiState.update {
                                        it.copy(userInfo = userInfo)
                                    }
                                    UserManager.saveUserInfo(userInfo)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * @desc：
 * @date：2023/04/24 17:28
 */
data class MeUiState(
    val isLoggedIn: Boolean = UserManager.isLoggedIn(),
    val userInfo: UserInfoReply.Data? = null,
)

/**
 * @desc：
 * @date：2023/04/25 14:00
 */
sealed class MeAction {
    object GetUserInfo : MeAction()
}