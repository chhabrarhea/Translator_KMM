package com.rhea.translator.di

import com.rhea.translator.database.TranslatorDatabase
import com.rhea.translator.domain.local.DatabaseDriverFactory
import com.rhea.translator.domain.local.SqlDelightHistoryDataSource
import com.rhea.translator.domain.remote.HttpClientFactory
import com.rhea.translator.domain.remote.TranslatorClient
import com.rhea.translator.domain.remote.TranslatorClientImpl
import com.rhea.translator.domain.usecase.Translate

class AppModule {
    val historyDataSource  by lazy {
        SqlDelightHistoryDataSource(
            TranslatorDatabase(
                DatabaseDriverFactory().create()
            )
        )
    }

    private val translateClient: TranslatorClient by lazy {
        TranslatorClientImpl(
            HttpClientFactory().create()
        )
    }

    val translateUseCase: Translate by lazy {
        Translate(translateClient, historyDataSource)
    }
}