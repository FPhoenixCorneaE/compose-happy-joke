package com.fphoenixcorneae.happyjoke.mvi.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.fphoenixcorneae.happyjoke.mvi.model.paging.JokeAuditListSource

/**
 * @desc：
 * @date：2023/05/17 16:26
 */
class JokeAuditViewModel : BaseViewModel<JokeAuditAction>() {
    /** 审核中帖子列表 */
    val jokeAuditingList = Pager(config = PagingConfig(pageSize = 10)) {
        JokeAuditListSource("0")
    }.flow.cachedIn(viewModelScope)

    /** 审核失败帖子列表 */
    val jokeAuditFailureList = Pager(config = PagingConfig(pageSize = 10)) {
        JokeAuditListSource("1")
    }.flow.cachedIn(viewModelScope)

    override fun dealIntent(action: JokeAuditAction) {
    }
}

sealed class JokeAuditAction {
}