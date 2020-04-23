package com.st.httpktx

import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * code bt St on 2020/4/8
 */

fun <T> GlobalScope.asyncWithLifecycleAsync(
    lifecycleOwner: LifecycleOwner,
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> T
): Deferred<T> {
    val deferred = GlobalScope.async(context, start) {
        block()
    }

    lifecycleOwner.lifecycle.addObserver(LifecycleCoroutineListener(deferred))
    return deferred
}

fun <T> GlobalScope.bindWithLifecycleAsync(lifecycleOwner: LifecycleOwner,
                                           block: CoroutineScope.() -> Deferred<T>): Deferred<T> {
    val deferred = block.invoke(this)

    lifecycleOwner.lifecycle.addObserver(LifecycleCoroutineListener(deferred))

    return deferred
}