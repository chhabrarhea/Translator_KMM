package com.rhea.translator.domain.utils

import kotlinx.coroutines.flow.Flow

expect class CommonFlow<T>(source: Flow<T>): Flow<T>

fun <T> Flow<T>.toCommonFlow() = CommonFlow(this)