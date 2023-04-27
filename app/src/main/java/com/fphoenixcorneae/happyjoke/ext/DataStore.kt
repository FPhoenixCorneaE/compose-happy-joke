package com.fphoenixcorneae.happyjoke.ext

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

/** Preferences DataStore 实例 */
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "prefs")

/**
 * 通过定义的key获取对应value的Flow
 *
 * @param key 键值对-key
 */
inline fun <reified T> Context.getDSFlow(key: String, defaultValue: T): Flow<T> = run {
    dataStore.data.map {
        it[getPreferencesKey<T>(key)] ?: defaultValue
    }
}

/**
 * 通过定义的key获取对应Set<String>value的Flow
 *
 * @param key 键值对-key
 */
fun Context.getStringSetDSFlow(key: String, defaultValue: Set<String>): Flow<Set<String>> = run {
    dataStore.data.map {
        it[stringSetPreferencesKey(key)] ?: defaultValue
    }
}

/**
 * 通过定义的key异步获取对应value
 *
 * @param key 键值对-key
 */
suspend inline fun <reified T> Context.getAsyncDS(key: String, defaultValue: T): T = run {
    getDSFlow(key = key, defaultValue).first()
}

/**
 * 通过定义的key异步获取对应Set<String>value
 *
 * @param key 键值对-key
 */
suspend fun Context.getAsyncDS(key: String, defaultValue: Set<String>): Set<String> = run {
    getStringSetDSFlow(key = key, defaultValue).first()
}

/**
 * 通过定义的key同步获取对应value
 *
 * @param key 键值对-key
 */
inline fun <reified T> Context.getSyncDS(key: String, defaultValue: T): T = run {
    runBlocking {
        getDSFlow(key = key, defaultValue).first()
    }
}

/**
 * 通过定义的key同步获取对应Set<String>value
 *
 * @param key 键值对-key
 */
fun Context.getSyncDS(key: String, defaultValue: Set<String>): Set<String> = run {
    runBlocking {
        getStringSetDSFlow(key = key, defaultValue).first()
    }
}

/**
 * 清空DataStore
 */
suspend fun Context.clearDS() = run {
    dataStore.edit {
        it.clear()
    }
}

/**
 * 通过定义的key存储对应的value
 *
 * @param key   键值对-key
 * @param value 键值对-value
 */
suspend inline fun <reified T> Context.saveDS(key: String, value: T) {
    saveDS(preferencesKey = getPreferencesKey(key), value = value)
}

/**
 * 通过定义的key存储Set<String>
 *
 * @param key      键值对-key
 * @param stringSetValue 键值对-value
 */
suspend fun Context.saveDS(key: String, stringSetValue: Set<String>) {
    saveDS(preferencesKey = stringSetPreferencesKey(key), value = stringSetValue)
}

/**
 * 通过定义的key存储对应的value
 *
 * @param preferencesKey
 * @param value
 */
suspend fun <T> Context.saveDS(preferencesKey: Preferences.Key<T>, value: T) {
    dataStore.edit { preferences ->
        preferences[preferencesKey] = value
    }
}

/**
 * 获取偏好key
 */
inline fun <reified T> getPreferencesKey(key: String): Preferences.Key<T> = run {
    when (T::class) {
        Int::class -> intPreferencesKey(key) as Preferences.Key<T>
        Long::class -> longPreferencesKey(key) as Preferences.Key<T>
        Float::class -> floatPreferencesKey(key) as Preferences.Key<T>
        Double::class -> doublePreferencesKey(key) as Preferences.Key<T>
        String::class -> stringPreferencesKey(key) as Preferences.Key<T>
        Boolean::class -> booleanPreferencesKey(key) as Preferences.Key<T>
        else -> throw IllegalArgumentException("This type ${T::class.java} can't be saved into DataStore.")
    }
}