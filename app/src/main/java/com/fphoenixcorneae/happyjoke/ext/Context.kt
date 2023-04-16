package com.fphoenixcorneae.happyjoke.ext

import android.content.Context
import android.view.ContextThemeWrapper
import androidx.activity.ComponentActivity

val Context.componentActivity: ComponentActivity?
    get() = when (this) {
        is ComponentActivity -> {
            this
        }
        is ContextThemeWrapper -> {
            this.baseContext.componentActivity
        }
        else -> {
            null
        }
    }