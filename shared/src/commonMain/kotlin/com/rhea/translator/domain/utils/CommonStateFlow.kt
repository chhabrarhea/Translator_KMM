package com.rhea.translator.domain.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/**
 * While generic type parameters of interfaces are lost in Ios translation,
 * generic type parameters of classes are preserved
 */
expect class CommonStateFlow<T>(flow: StateFlow<T>): StateFlow<T>

fun <T> StateFlow<T>.toCommonStateFlow() = CommonStateFlow(this)