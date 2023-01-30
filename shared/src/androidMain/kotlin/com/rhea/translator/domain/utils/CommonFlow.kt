package com.rhea.translator.domain.utils

import kotlinx.coroutines.flow.Flow

actual class CommonFlow<T> actual constructor(
    private val source: Flow<T>
) : Flow<T> by source