package com.rhea.translator.domain.utils

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow

actual open class CommonFlow<T> actual constructor(
    private val source: Flow<T>
): Flow<T> by source {

    fun subscribe(
        coroutineScope: CoroutineScope,
        dispatcher: CoroutineDispatcher,
        onCollect: (T) -> Unit
    ): DisposableHandle {
        val job = coroutineScope.launch(dispatcher) {
            source.collect(onCollect)
        }
        return object : DisposableHandle {
            override fun dispose() {
                job.cancel()
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun collect(onCollect: (T) -> Unit) : DisposableHandle {
        return subscribe(GlobalScope, Dispatchers.Main, onCollect)
    }
}