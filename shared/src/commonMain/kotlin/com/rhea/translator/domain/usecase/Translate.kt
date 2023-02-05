package com.rhea.translator.domain.usecase

import com.rhea.translator.domain.local.HistoryDataSource
import com.rhea.translator.domain.models.HistoryItem
import com.rhea.translator.domain.models.Language
import com.rhea.translator.domain.remote.TranslateException
import com.rhea.translator.domain.remote.TranslatorClient
import com.rhea.translator.domain.utils.Resource

class Translate(
    private val client: TranslatorClient,
    private val historyDataSource: HistoryDataSource
) {

    suspend fun execute(
        fromLanguage: Language,
        fromText: String,
        toLanguage: Language
    ): Resource<String> {
        return try {
            val translatedText = client.translateText(
                fromLanguage, toLanguage, fromText
            )

            historyDataSource.insertHistory(
                HistoryItem(
                    id = null,
                    fromLanguageCode = fromLanguage.langCode,
                    fromText = fromText,
                    toLanguageCode = toLanguage.langCode,
                    toText = translatedText,
                )
            )
            Resource.Success(translatedText)
        } catch(e: TranslateException) {
            e.printStackTrace()
            Resource.Error(e)
        }
    }
}