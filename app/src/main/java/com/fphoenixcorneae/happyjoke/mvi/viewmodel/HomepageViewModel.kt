package com.fphoenixcorneae.happyjoke.mvi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.fphoenixcorneae.happyjoke.ext.launchIo
import com.fphoenixcorneae.happyjoke.https.apiService
import com.fphoenixcorneae.happyjoke.https.doOnSuccess
import com.fphoenixcorneae.happyjoke.https.httpRequest
import com.fphoenixcorneae.happyjoke.https.send
import com.fphoenixcorneae.happyjoke.mvi.model.AttentionRecommend
import com.fphoenixcorneae.happyjoke.mvi.model.paging.*
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

    /** 首页最新列表 */
    val homepageLatests = Pager(config = PagingConfig(pageSize = 10)) {
        HomepageLatestSource()
    }.flow.cachedIn(viewModelScope)

    /** 首页纯文列表 */
    val homepagePoliteLetters = Pager(config = PagingConfig(pageSize = 10)) {
        HomepagePoliteLettersSource()
    }.flow.cachedIn(viewModelScope)

    /** 首页纯图片列表 */
    val homepageFunnyPictures = Pager(config = PagingConfig(pageSize = 10)) {
        HomepageFunnyPicturesSource()
    }.flow.cachedIn(viewModelScope)

    /** 划一划页面的推荐列表 */
    val sweepTikTokVideos = Pager(config = PagingConfig(pageSize = 10)) {
        SweepTikTokSource()
    }.flow.cachedIn(viewModelScope)

    private val _attentionUiState = MutableStateFlow(AttentionUiState())
    val attentionUiState = _attentionUiState.asStateFlow()

    /**
     * 获取首页的推荐关注数据
     */
    fun getAttentionRecommend() {
        launchIo {
            httpRequest {
                apiService.homepageAttentionRecommend()
            }.doOnSuccess { result ->
                _attentionUiState.update {
                    it.copy(attentionRecommend = result?.data)
                }
            }.send()
        }
    }
}

/**
 * @desc：定义页面状态的时候，每个属性值都是不可变的，如果需要结合多个状态判断做页面展示，这种判断可直接在UiState扩展。
 * @date：2023/04/20 17:45
 */
data class AttentionUiState(
    val attentionRecommend: List<AttentionRecommend.Data>? = null,
)
