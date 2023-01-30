package com.rhea.translator.domain.utils

import kotlinx.coroutines.flow.Flow

/**
 * Flow<T> is the interface all asynchronous flows in Kotlin implement.
 * While generic interfaces are lost in ios translation, generic classes are preserved.
 */
expect class CommonFlow<T>(source: Flow<T>): Flow<T>

fun <T> Flow<T>.toCommonFlow() = CommonFlow(this)