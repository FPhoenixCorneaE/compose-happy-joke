package com.fphoenixcorneae.happyjoke.mvi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.fphoenixcorneae.happyjoke.mvi.model.paging.HomepageRecommendSource
import com.fphoenixcorneae.happyjoke.mvi.model.paging.SweepDouYinSource

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
}