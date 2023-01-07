package com.rhea.translator.domain.remote

import com.rhea.translator.domain.models.Language
import com.rhea.translator.domain.models.TranslateRequest
import com.rhea.translator.domain.models.TranslatedResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.utils.io.errors.*

class TranslatorClientImpl(
    private val httpClient: HttpClient
) : TranslatorClient {
    override suspend fun translateText(
        fromLanguage: Language,
        toLanguage: Language,
        text: String
    ): String {
        val result = try {
            httpClient.post {
                url("https://translate.pl-coding.com/translate")
                contentType(ContentType.Application.Json)
                setBody(
                    TranslateRequest(text, fromLanguage.langCode, toLanguage.langCode)
                )
            }
        } catch (exception: IOException) {
            throw TranslateException(TranslateError.NETWORK_ERROR)
        }
        when (result.status.value) {
            in 200..299 -> Unit
            500 -> throw TranslateException(TranslateError.SERVER_ERROR)
            in 400..499 -> throw TranslateException(TranslateError.CLIENT_ERROR)
            else -> throw TranslateException(TranslateError.UNKNOWN_ERROR)
        }

        return try {
            result.body<TranslatedResponse>().translatedText
        } catch (e: Exception) {
            throw TranslateException(TranslateError.SERVER_ERROR)
        }
    }
}