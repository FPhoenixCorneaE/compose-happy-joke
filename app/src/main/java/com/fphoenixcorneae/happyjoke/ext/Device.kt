package com.fphoenixcorneae.happyjoke.ext

import android.os.Build

/**
 * Return the manufacturer of the product/hardware.
 * e.g. Xiaomi
 */
val deviceManufacturer: String
    get() = Build.MANUFACTURER

/**
 * Return the model of device.
 *
 * e.g. MI2SC
 *
 * @return the model of device
 */
val deviceModel: String
    get() {
        var model = Build.MODEL
        model = model?.trim { it <= ' ' }?.replace("\\s*".toRegex(), "") ?: ""
        return model
    }