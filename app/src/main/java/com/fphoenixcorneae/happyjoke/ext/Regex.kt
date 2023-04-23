package com.fphoenixcorneae.happyjoke.ext

/**
 * ^1[3-9]：以数字1开头，第二个数字是3-9中的任意一个
 * \d{9}$：后面跟着任意9个数字，并且以这9个数字结尾
 */
fun CharSequence?.isMobilePhone(): Boolean = this?.runCatching {
    val regex = "^1[3-9]\\d{9}\$"
    Regex(pattern = regex).matches(input = this)
}?.getOrDefault(false) ?: false