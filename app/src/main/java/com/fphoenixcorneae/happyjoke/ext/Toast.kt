package com.fphoenixcorneae.happyjoke.ext

import android.widget.Toast
import com.fphoenixcorneae.happyjoke.startup.applicationContext

fun CharSequence.toast() {
    Toast.makeText(applicationContext, this, Toast.LENGTH_SHORT).show()
}