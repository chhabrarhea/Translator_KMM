package com.rhea.translator.domain.models

@kotlinx.serialization.Serializable
data class TranslatedResponse(
    val translatedText: String
)