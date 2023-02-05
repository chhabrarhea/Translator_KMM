package com.rhea.translator.domain.utils

sealed class Resource<T>(val data: T?, val error: Throwable? ) {
    class Success<T>(data: T): Resource<T>(data, null)
    class Error<T>(throwable: Throwable): Resource<T>(null, throwable)
}