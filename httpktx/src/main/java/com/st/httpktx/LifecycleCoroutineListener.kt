package com.st.httpktx

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import kotlinx.coroutines.Job
import okhttp3.OkHttpClient

/**
 * code bt St on 2020/3/4
 */
class LifecycleCoroutineListener(
    private val job: Job,
    private val cancelEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY
) :
    LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun pause() = handleEvent(Lifecycle.Event.ON_PAUSE)

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() = handleEvent(Lifecycle.Event.ON_STOP)

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun destroy() = handleEvent(Lifecycle.Event.ON_DESTROY)

    private fun handleEvent(e: Lifecycle.Event) {
        if (e == cancelEvent && !job.isCancelled) {
            job.cancel()
        }
    }
}