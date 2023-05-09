package com.fphoenixcorneae.happyjoke.ext

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridItemSpanScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.runtime.Composable
import androidx.paging.compose.LazyPagingItems

/**
 * LazyVerticalGrid使用paging分页扩展方法
 */
inline fun <T : Any> LazyGridScope.items(
    items: LazyPagingItems<T>,
    noinline key: ((item: T) -> Any)? = null,
    noinline span: (LazyGridItemSpanScope.(item: T?) -> GridItemSpan)? = null,
    noinline contentType: (item: T?) -> Any? = { null },
    crossinline itemContent: @Composable LazyGridItemScope.(item: T?) -> Unit,
) {
    items(
        count = items.itemCount,
        key = if (key == null) null else { index ->
            val item = items.peek(index)
            if (item == null) {
                MyPagingPlaceholderKey(index)
            } else {
                key(item)
            }
        },
        span = if (span != null) {
            { span(items[it]) }
        } else null,
        contentType = { index: Int -> contentType(items[index]) }
    ) { index ->
        itemContent(items[index])
    }
}

inline fun <T : Any> LazyGridScope.itemsIndexed(
    items: LazyPagingItems<T>,
    noinline key: ((index: Int, item: T) -> Any)? = null,
    noinline span: (LazyGridItemSpanScope.(index: Int, item: T?) -> GridItemSpan)? = null,
    crossinline contentType: (index: Int, item: T?) -> Any? = { _, _ -> null },
    crossinline itemContent: @Composable LazyGridItemScope.(index: Int, value: T?) -> Unit,
) {
    items(
        count = items.itemCount,
        key = if (key == null) null else { index ->
            val item = items.peek(index)
            if (item == null) {
                MyPagingPlaceholderKey(index)
            } else {
                key(index, item)
            }
        },
        span = if (span != null) {
            { span(it, items[it]) }
        } else null,
        contentType = { index -> contentType(index, items[index]) }
    ) { index ->
        itemContent(index, items[index])
    }
}

@SuppressLint("BanParcelableUsage")
data class MyPagingPlaceholderKey(private val index: Int) : Parcelable {
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(index)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @Suppress("unused")
        @JvmField
        val CREATOR: Parcelable.Creator<MyPagingPlaceholderKey> =
            object : Parcelable.Creator<MyPagingPlaceholderKey> {
                override fun createFromParcel(parcel: Parcel) =
                    MyPagingPlaceholderKey(parcel.readInt())

                override fun newArray(size: Int) = arrayOfNulls<MyPagingPlaceholderKey?>(size)
            }
    }
}