package com.fphoenixcorneae.happyjoke.mvi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fphoenixcorneae.happyjoke.https.apiService
import com.fphoenixcorneae.happyjoke.mvi.model.HomepageRecommend
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * @desc：
 * @date：2023/03/21 17:48
 */
class HomepageViewModel : ViewModel() {
    private val _state = MutableStateFlow(HomepageState())
    val state = _state.asStateFlow()

    fun refresh() {
        _state.update {
            it.copy().apply {
                isRefreshing = true
                isLoading = true
            }
        }
        homeRecommend()
    }

    /**
     * 获取首页的推荐列表数据
     */
    private fun homeRecommend() {
        viewModelScope.launch {
            runCatching {
                apiService.homepageRecommend().let { recommend ->
                    _state.update {
                        it.copy().apply {
                            homepageRecommend = recommend
                            isRefreshing = false
                            delay(400)
                            isLoading = false
                        }
                    }
                }
            }.onFailure {
                it.printStackTrace()
                _state.update {
                    it.copy().apply {
                        isRefreshing = true
                        delay(400)
                        isLoading = false
                    }
                }
            }.getOrNull()
        }
    }

    init {
        homeRecommend()
    }
}

/**
 * @desc：
 * @date：2023/03/22 14:32
 */
data class HomepageState(
    var homepageRecommend: HomepageRecommend? = null,
    var isRefreshing: Boolean = true,
    var isLoading: Boolean = true,
)