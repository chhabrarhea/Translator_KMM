package com.rhea.translator.domain.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
expect class CommonStateFlow<T>(flow: StateFlow<T>): StateFlow<T>

fun <T> StateFlow<T>.toCommonStateFlow() = CommonStateFlow(this)