package com.fphoenixcorneae.happyjoke.mvi.viewmodel

import com.fphoenixcorneae.happyjoke.ext.isMobilePhone
import com.fphoenixcorneae.happyjoke.ext.launchDefault
import com.fphoenixcorneae.happyjoke.ext.launchIo
import com.fphoenixcorneae.happyjoke.ext.toast
import com.fphoenixcorneae.happyjoke.https.doOnError
import com.fphoenixcorneae.happyjoke.https.doOnSuccess
import com.fphoenixcorneae.happyjoke.https.httpRequest
import com.fphoenixcorneae.happyjoke.https.userService
import com.fphoenixcorneae.happyjoke.mvi.model.BaseReply
import com.fphoenixcorneae.happyjoke.tool.UserManager
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update

/**
 * @desc：
 * @date：2023/04/23 13:48
 */
class LoginViewModel : BaseViewModel<LoginAction>() {

    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState = _loginUiState.asStateFlow()

    fun toggleLoginMode() {
        launchDefault {
            _loginUiState.update {
                it.copy(isAuthCodeLogin = !it.isAuthCodeLogin)
            }
        }
    }

    fun accountChanged(value: String) {
        launchDefault {
            _loginUiState.update {
                it.copy(account = value)
            }
        }
    }

    fun authCodeChanged(value: String) {
        launchDefault {
            _loginUiState.update {
                it.copy(authCode = value)
            }
        }
    }

    fun passwordChanged(value: String) {
        launchDefault {
            _loginUiState.update {
                it.copy(password = value)
            }
        }
    }

    override fun dealIntent(action: LoginAction) {
        when (action) {
            LoginAction.GetCode -> launchIo {
                httpRequest {
                    userService.getCode(loginUiState.first().account)
                }.doOnSuccess { reply ->
                    if (reply?.code == 0) {
                        reply.msg?.toast()
                    } else if (reply?.code == BaseReply.OK) {
                        _loginUiState.update {
                            it.copy(sendCodeTime = System.currentTimeMillis().toString())
                        }
                    }
                }.doOnError {
                    it.message?.toast()
                }
            }
            LoginAction.Login -> launchIo {
                httpRequest {
                    loginUiState.first().let {
                        if (it.isAuthCodeLogin) {
                            userService.loginByCode(it.account, it.authCode)
                        } else {
                            userService.loginByPsw(it.account, it.password)
                        }
                    }
                }.doOnSuccess { reply ->
                    if (reply?.code == 0) {
                        reply.msg?.toast()
                    } else if (reply?.code == BaseReply.OK) {
                        UserManager.saveToken(reply.data?.token)
                            .loginState(true)
                        _loginUiState.update {
                            it.copy(loginSuccess = true)
                        }
                    }
                }.doOnError {
                    it.message?.toast()
                }
            }
            LoginAction.ToggleEncounterProblemDialog -> launchDefault {
                _loginUiState.update {
                    it.copy(showEncounterProblemDialog = !it.showEncounterProblemDialog)
                }
            }
        }
    }
}

/**
 * @desc：
 * @date：2023/04/23 13:51
 */
data class LoginUiState(
    val account: String = "",
    val authCode: String = "",
    val password: String = "",
    val isAuthCodeLogin: Boolean = true,
    val showEncounterProblemDialog: Boolean = false,
    val sendCodeTime: String? = null,
    val loginSuccess: Boolean = false,
) {
    fun isMobilePhone() = account.isMobilePhone()

    fun loginEnabled() = if (isAuthCodeLogin) {
        isMobilePhone() && authCode.length >= 6
    } else {
        isMobilePhone() && password.length >= 6
    }
}

/**
 * @desc：
 * @date：2023/04/23 13:55
 */
sealed class LoginAction {
    /** 获取验证码 */
    object GetCode : LoginAction()

    /** 登录 */
    object Login : LoginAction()

    /** 显示或隐藏遇到问题弹窗 */
    object ToggleEncounterProblemDialog : LoginAction()
}