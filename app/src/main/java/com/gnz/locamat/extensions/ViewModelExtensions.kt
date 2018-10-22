package com.gnz.locamat.extensions

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer


fun <T : Any, L : LiveData<T>> LifecycleOwner.observe(liveData: L, body: (T) -> Unit) {
    liveData.observe(this, Observer { it?.apply(body) })
}

fun <T : Any, L : LiveData<T>> LifecycleOwner.stopObserve(liveData: L, body: (T) -> Unit) {
    liveData.removeObserver({ it?.apply(body) })
}
