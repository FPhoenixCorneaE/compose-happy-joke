package com.fphoenixcorneae.happyjoke.mvi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fphoenixcorneae.happyjoke.https.apiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * @desc：
 * @date：2023/03/21 17:48
 */
class HomepageViewModel : ViewModel() {
    private val _state = MutableStateFlow<HomepageState?>(null)
    val state = _state.asStateFlow()

    /**
     * 获取主页的推荐列表数据
     */
    private fun homeRecommend(){
        viewModelScope.launch {
            runCatching {
                apiService.homeRecommend()
            }.onFailure {
                it.printStackTrace()
            }.getOrNull()
        }
    }

    init {
        homeRecommend()
    }
}

data class HomepageState(
    val any: Any,
) {

}