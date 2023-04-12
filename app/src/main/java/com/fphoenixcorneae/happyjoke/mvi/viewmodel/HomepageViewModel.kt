package com.fphoenixcorneae.happyjoke.mvi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.fphoenixcorneae.happyjoke.mvi.model.paging.HomepageRecommendSource

/**
 * @desc：
 * @date：2023/03/21 17:48
 */
class HomepageViewModel : ViewModel() {

    val homepageRecommends = Pager(config = PagingConfig(pageSize = 10)) {
        HomepageRecommendSource()
    }.flow.cachedIn(viewModelScope)
}