package com.fphoenixcorneae.happyjoke.mvi.viewmodel

import com.fphoenixcorneae.happyjoke.ext.launchIo
import com.fphoenixcorneae.happyjoke.https.doOnSuccess
import com.fphoenixcorneae.happyjoke.https.httpRequest
import com.fphoenixcorneae.happyjoke.https.userService
import com.fphoenixcorneae.happyjoke.mvi.model.BaseReply
import com.fphoenixcorneae.happyjoke.mvi.model.UserInfoReply
import com.fphoenixcorneae.happyjoke.tool.UserManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * @desc：
 * @date：2023/04/24 17:00
 */
class MeViewModel : BaseViewModel<MeAction>() {
    private val _meUiState = MutableStateFlow(MeUiState())
    val meUiState = _meUiState.asStateFlow()

    override fun dealIntent(action: MeAction) {
        when (action) {
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

/**
 * @desc：
 * @date：2023/04/24 17:28
 */
data class MeUiState(
    val loginStateFlow: Flow<Boolean> = UserManager.loginStateFlow(),
    val userInfo: UserInfoReply.Data? = null,
)

/**
 * @desc：
 * @date：2023/04/25 14:00
 */
sealed class MeAction {

    /** 获取用户信息 */
    object GetUserInfo : MeAction()
}