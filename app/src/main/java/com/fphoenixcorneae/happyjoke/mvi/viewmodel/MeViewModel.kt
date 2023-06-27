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
                            UserManager.saveUserInfo(userInfo)
                        }
                    } else if (reply?.code == BaseReply.LOGIN_EXPIRE) {
                        UserManager.loginExpire()
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
    val isLoggedIn: Flow<Boolean> = UserManager.loginStateFlow(),
    val userInfo: Flow<UserInfoReply.Data?> = UserManager.userInfoFlow(),
)

/**
 * @desc：
 * @date：2023/04/25 14:00
 */
sealed class MeAction {

    /** 获取用户信息 */
    object GetUserInfo : MeAction()
}