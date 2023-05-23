package com.rhea.translator.android.di

import android.app.Application
import com.rhea.translator.android.voice_to_text.AndroidTextToVoiceParser
import com.rhea.translator.voice_to_text.VoiceToTextParser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(ViewModelComponent::class)
class VoiceToTextModule {

    @Provides
    @ViewModelScoped
    fun provideVoiceToTextParser(app: Application): VoiceToTextParser {
        return AndroidTextToVoiceParser(app)
    }
}