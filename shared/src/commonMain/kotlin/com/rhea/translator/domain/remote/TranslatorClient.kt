package com.rhea.translator.domain.remote

import com.rhea.translator.domain.models.Language

interface TranslatorClient {
    suspend fun translateText(
        fromLanguage: Language,
        toLanguage: Language,
        text: String
    ): String
}