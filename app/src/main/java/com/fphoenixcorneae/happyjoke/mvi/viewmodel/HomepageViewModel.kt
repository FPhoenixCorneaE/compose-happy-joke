package com.fphoenixcorneae.happyjoke.mvi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.fphoenixcorneae.happyjoke.ext.launchIo
import com.fphoenixcorneae.happyjoke.https.apiService
import com.fphoenixcorneae.happyjoke.https.doOnSuccess
import com.fphoenixcorneae.happyjoke.https.sendHttpRequest
import com.fphoenixcorneae.happyjoke.mvi.model.AttentionRecommend
import com.fphoenixcorneae.happyjoke.mvi.model.paging.HomepageRecommendSource
import com.fphoenixcorneae.happyjoke.mvi.model.paging.SweepDouYinSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * @desc：
 * @date：2023/03/21 17:48
 */
class HomepageViewModel : ViewModel() {

    /** 首页推荐列表 */
    val homepageRecommends = Pager(config = PagingConfig(pageSize = 10)) {
        HomepageRecommendSource()
    }.flow.cachedIn(viewModelScope)

    /** 划一划页面的推荐列表 */
    val sweepDouYinVideos = Pager(config = PagingConfig(pageSize = 10)) {
        SweepDouYinSource()
    }.flow.cachedIn(viewModelScope)

    private val _attentionUiState = MutableStateFlow(AttentionUiState())
    val attentionUiState = _attentionUiState.asStateFlow()

    /**
     * 获取首页的推荐关注数据
     */
    fun getAttentionRecommend() {
        launchIo {
            sendHttpRequest {
                apiService.homepageAttentionRecommend()
            }.doOnSuccess { result ->
                _attentionUiState.update {
                    it.copy().apply { attentionRecommend = result?.data }
                }
            }
        }
    }
}

data class AttentionUiState(
    var attentionRecommend: List<AttentionRecommend.Data>? = null,
)
