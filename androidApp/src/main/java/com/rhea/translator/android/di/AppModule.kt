package com.rhea.translator.android.di

import android.app.Application
import com.rhea.translator.database.TranslatorDatabase
import com.rhea.translator.domain.local.DatabaseDriverFactory
import com.rhea.translator.domain.local.HistoryDataSource
import com.rhea.translator.domain.local.SqlDelightHistoryDataSource
import com.rhea.translator.domain.remote.HttpClientFactory
import com.rhea.translator.domain.remote.TranslatorClient
import com.rhea.translator.domain.remote.TranslatorClientImpl
import com.rhea.translator.domain.usecase.Translate
import com.squareup.sqldelight.db.SqlDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClientFactory().create()
    }

    @Provides
    @Singleton
    fun provideTranslateClient(httpClient: HttpClient): TranslatorClient {
        return TranslatorClientImpl(httpClient)
    }

    @Provides
    @Singleton
    fun provideDatabaseDriver(app: Application): SqlDriver {
        return DatabaseDriverFactory(app).create()
    }

    @Provides
    @Singleton
    fun provideHistoryDataSource(driver: SqlDriver): HistoryDataSource {
        return SqlDelightHistoryDataSource(TranslatorDatabase(driver))
    }

    @Provides
    @Singleton
    fun provideTranslateUseCase(
        client: TranslatorClient,
        dataSource: HistoryDataSource
    ): Translate {
        return Translate(client, dataSource)
    }
}