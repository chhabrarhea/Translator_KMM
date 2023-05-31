package com.rhea.translator.android.voice_to_text.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rhea.translator.voice_to_text.VoiceToTextEvent
import com.rhea.translator.voice_to_text.VoiceToTextParser
import com.rhea.translator.voice_to_text.VoiceToTextViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AndroidVoiceToTextViewModel @Inject constructor(parser: VoiceToTextParser): ViewModel() {

    private val viewModel by lazy {
        VoiceToTextViewModel(parser, viewModelScope)
    }

    val state = viewModel.state

    fun onEvent(event: VoiceToTextEvent) {
        viewModel.onEvent(event)
    }
}