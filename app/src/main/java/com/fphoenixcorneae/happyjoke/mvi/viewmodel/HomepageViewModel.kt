package com.fphoenixcorneae.happyjoke.mvi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fphoenixcorneae.happyjoke.https.apiService
import com.fphoenixcorneae.happyjoke.mvi.model.HomepageRecommend
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * @desc：
 * @date：2023/03/21 17:48
 */
class HomepageViewModel : ViewModel() {
    private val _state = MutableStateFlow(HomepageState())
    val state = _state.asStateFlow()

    /**
     * 获取首页的推荐列表数据
     */
    private fun homeRecommend() {
        viewModelScope.launch {
            runCatching {
                apiService.homepageRecommend().let {
                    _state.emit(_state.first().copy().apply { homepageRecommend = it })
                }
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
    var homepageRecommend: HomepageRecommend? = null,
)