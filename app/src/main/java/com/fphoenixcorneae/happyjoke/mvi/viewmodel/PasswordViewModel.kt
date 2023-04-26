package com.fphoenixcorneae.happyjoke.mvi.viewmodel

import androidx.lifecycle.ViewModel
import com.fphoenixcorneae.happyjoke.ext.isMobilePhone
import com.fphoenixcorneae.happyjoke.ext.launchDefault
import com.fphoenixcorneae.happyjoke.ext.launchIo
import com.fphoenixcorneae.happyjoke.ext.toast
import com.fphoenixcorneae.happyjoke.https.doOnError
import com.fphoenixcorneae.happyjoke.https.doOnSuccess
import com.fphoenixcorneae.happyjoke.https.httpRequest
import com.fphoenixcorneae.happyjoke.https.userService
import com.fphoenixcorneae.happyjoke.mvi.model.BaseReply
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

/**
 * @desc：
 * @date：2023/04/26 09:42
 */
class PasswordViewModel : ViewModel() {

    private val _passwordUiState = MutableStateFlow(PasswordUiState())
    val passwordUiState = _passwordUiState.asStateFlow()
    private val passwordAction = Channel<PasswordAction>()

    fun accountChanged(value: String) {
        launchDefault {
            _passwordUiState.update {
                it.copy(account = value)
            }
        }
    }

    fun authCodeChanged(value: String) {
        launchDefault {
            _passwordUiState.update {
                it.copy(authCode = value)
            }
        }
    }

    fun passwordChanged(value: String) {
        launchDefault {
            _passwordUiState.update {
                it.copy(password = value)
            }
        }
    }

    fun dispatchIntent(action: PasswordAction) {
        launchDefault {
            passwordAction.send(action)
        }
    }

    init {
        launchDefault {
            passwordAction.receiveAsFlow().collect {
                when (it) {
                    PasswordAction.GetCode -> launchIo {
                        httpRequest {
                            userService.passwordResetGetCode(passwordUiState.first().account)
                        }.doOnSuccess { reply ->
                            if (reply?.code == 0) {
                                reply.msg?.toast()
                            } else if (reply?.code == BaseReply.OK) {
                                "验证码发送成功".toast()
                            }
                        }.doOnError {
                            it.message?.toast()
                        }
                    }
                    PasswordAction.Reset -> launchIo {
                        httpRequest {
                            passwordUiState.first().let {
                                userService.passwordReset(it.account, it.authCode, it.password)
                            }
                        }.doOnSuccess { reply ->
                            if (reply?.code == 0) {
                                reply.msg?.toast()
                            } else if (reply?.code == BaseReply.OK) {
                                "重置密码成功".toast()
                                _passwordUiState.update {
                                    it.copy(pswResetSuccess = true)
                                }
                            }
                        }.doOnError {
                            it.message?.toast()
                        }
                    }
                    PasswordAction.ToggleEncounterProblemDialog -> launchDefault {
                        _passwordUiState.update {
                            it.copy(showEncounterProblemDialog = !it.showEncounterProblemDialog)
                        }
                    }
                }
            }
        }
    }
}

/**
 * @desc：
 * @date：2023/04/26 09:42
 */
data class PasswordUiState(
    val account: String = "",
    val authCode: String = "",
    val password: String = "",
    val showEncounterProblemDialog: Boolean = false,
    val pswResetSuccess: Boolean = false,
) {
    fun isMobilePhone() = account.isMobilePhone()

    fun resetEnabled() = isMobilePhone() && authCode.length >= 6 && password.length in 6..18
}

/**
 * @desc：
 * @date：2023/04/26 09:42
 */
sealed class PasswordAction {
    /** 获取验证码 */
    object GetCode : PasswordAction()

    /** 重置 */
    object Reset : PasswordAction()

    /** 显示或隐藏遇到问题弹窗 */
    object ToggleEncounterProblemDialog : PasswordAction()
}