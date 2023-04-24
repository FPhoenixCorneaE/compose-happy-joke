package com.fphoenixcorneae.happyjoke.ext

import android.content.pm.PackageManager
import android.os.Build
import com.fphoenixcorneae.happyjoke.startup.applicationContext

val appPackageName: String
    get() =
        applicationContext.packageName

val appVersionName: String
    get() = runCatching {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            applicationContext.packageManager.getPackageInfo(
                appPackageName, PackageManager.PackageInfoFlags.of(0)
            ).versionName
        } else {
            applicationContext.packageManager.getPackageInfo(
                appPackageName, 0
            ).versionName
        }
    }.onFailure {
        it.printStackTrace()
    }.getOrDefault("")

val appVersionCode: Long
    get() =
        runCatching {
            applicationContext.packageManager.getPackageInfo(appPackageName, 0).run {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    longVersionCode
                } else {
                    versionCode.toLong()
                }
            }
        }.onFailure {
            it.printStackTrace()
        }.getOrDefault(0)

val appUid: Int
    get() =
        runCatching {
            applicationContext.packageManager.getApplicationInfo(appPackageName, 0).uid
        }.onFailure {
            it.printStackTrace()
        }.getOrDefault(-1)