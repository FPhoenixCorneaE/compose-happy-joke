package com.fphoenixcorneae.happyjoke.mvi.viewmodel

import androidx.lifecycle.ViewModel
import com.fphoenixcorneae.happyjoke.mvi.model.LoginReply
import com.fphoenixcorneae.happyjoke.tool.UserManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * @desc：
 * @date：2023/04/24 17:00
 */
class MeViewModel : ViewModel() {
    private val _meUiState = MutableStateFlow(MeUiState())
    val meUiState = _meUiState.asStateFlow()
}

/**
 * @desc：
 * @date：2023/04/24 17:28
 */
data class MeUiState(
    val userInfo: LoginReply.Data.UserInfo? = UserManager.getUserInfo(),
    val isLoggedIn: Boolean = UserManager.isLoggedIn(),
)