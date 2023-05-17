package com.fphoenixcorneae.happyjoke.ext

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.lang.reflect.Type

private val GSON: Gson = createGsonBuilder(true).create()
private val GSON_NO_NULLS: Gson = createGsonBuilder(false).create()

fun createGsonBuilder(serializeNulls: Boolean = true): GsonBuilder {
    val builder = GsonBuilder()
    if (serializeNulls) {
        // 如果不设置 serializeNulls,序列化时默认忽略Null
        builder.serializeNulls()
    }
    return builder
        // 使打印的 json 字符串更美观，如果不设置，打印出来的字符串不分行
        .setPrettyPrinting()
}

fun Any?.toJson(
    includeNulls: Boolean = true,
): String {
    return when {
        includeNulls -> GSON.toJson(this)
        else -> GSON_NO_NULLS.toJson(this)
    }
}

fun <T> String?.toObject(clazz: Class<T>): T? {
    return runCatching {
        GSON.fromJson(this, clazz)
    }.onFailure {
        it.printStackTrace()
    }.getOrNull()
}

fun <T> String?.toObject(type: Type): T? {
    return runCatching {
        GSON.fromJson<T>(this, type)
    }.onFailure {
        it.printStackTrace()
    }.getOrNull()
}