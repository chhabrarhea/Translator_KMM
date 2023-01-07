package com.rhea.translator.domain.remote

enum class TranslateError {
    NETWORK_ERROR,
    SERVER_ERROR,
    CLIENT_ERROR,
    UNKNOWN_ERROR
}

class TranslateException(val error: TranslateError): Exception(
    message = "An error occurred when translating: $error"
)