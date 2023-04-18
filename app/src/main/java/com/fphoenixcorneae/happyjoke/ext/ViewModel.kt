package com.fphoenixcorneae.happyjoke.ext

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun ViewModel.launchDefault(block: suspend () -> Unit) =
    viewModelScope.launch(Dispatchers.Default) {
        block()
    }

fun ViewModel.launchIo(block: suspend () -> Unit) =
    viewModelScope.launch(Dispatchers.IO) {
        block()
    }

fun ViewModel.launchMain(block: suspend () -> Unit) =
    viewModelScope.launch(Dispatchers.Main) {
        block()
    }

