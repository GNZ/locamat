package com.gnz.locamat.common

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.support.annotation.MainThread


class ReactiveLiveData<T : Any?>(private val source: LiveData<T>) {
    companion object {
        @MainThread
        @JvmStatic
        fun <T, R> switchMap(source: LiveData<T>, func: (T) -> LiveData<R>?): LiveData<R> {
            return Transformations.switchMap(source, func)
        }

        @MainThread
        @JvmStatic
        fun <T> switchLatest(source: LiveData<out LiveData<T>?>): LiveData<T> {
            return switchMap(source) { it }
        }
    }
}

fun <T> LiveData<out LiveData<T>?>.switchLatest(): LiveData<T> =
        ReactiveLiveData.switchLatest(this)