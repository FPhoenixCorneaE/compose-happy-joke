package com.fphoenixcorneae.happyjoke.mvi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.fphoenixcorneae.happyjoke.ext.launchIo
import com.fphoenixcorneae.happyjoke.https.doOnSuccess
import com.fphoenixcorneae.happyjoke.https.httpRequest
import com.fphoenixcorneae.happyjoke.https.jokeService
import com.fphoenixcorneae.happyjoke.mvi.model.JokeDetailsReply
import com.fphoenixcorneae.happyjoke.mvi.model.paging.JokeCommentListSource
import com.fphoenixcorneae.happyjoke.mvi.model.paging.JokeLikeListSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * @desc：
 * @date：2023/05/10 11:17
 */
class JokeDetailsViewModelFactory(private val targetJokeId: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JokeDetailsViewModel::class.java)) {
            return JokeDetailsViewModel(targetJokeId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

/**
 * @desc：
 * @date：2023/05/10 10:47
 */
class JokeDetailsViewModel(
    private val targetJokeId: String,
) : BaseViewModel<JokeDetailsAction>() {
    /** 段子评论列表 */
    val jokeCommentList = Pager(config = PagingConfig(pageSize = 10)) {
        JokeCommentListSource(targetJokeId = targetJokeId)
    }.flow.cachedIn(viewModelScope)

    /** 段子点赞列表 */
    val jokeLikeList = Pager(config = PagingConfig(pageSize = 10)) {
        JokeLikeListSource(targetJokeId = targetJokeId)
    }.flow.cachedIn(viewModelScope)

    private val _jokeDetailsUiState = MutableStateFlow(JokeDetailsUiState())
    val jokeDetailsUiState = _jokeDetailsUiState.asStateFlow()

    override fun dealIntent(action: JokeDetailsAction) {
        when (action) {
            JokeDetailsAction.GetJokeDetails -> launchIo {
                httpRequest {
                    jokeService.getJokeById(jokeId = targetJokeId)
                }.doOnSuccess { reply ->
                    _jokeDetailsUiState.update {
                        it.copy(joke = reply?.data)
                    }
                }
            }
        }
    }
}

data class JokeDetailsUiState(
    val joke: JokeDetailsReply.Data? = null,
)

sealed class JokeDetailsAction {
    /** 获取贴子详情 */
    object GetJokeDetails : JokeDetailsAction()
}