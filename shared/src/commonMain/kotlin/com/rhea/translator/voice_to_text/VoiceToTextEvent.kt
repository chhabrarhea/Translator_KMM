package com.rhea.translator.voice_to_text

sealed class VoiceToTextEvent {
    object Close: VoiceToTextEvent()
    data class PermissionResult(
        val isGranted: Boolean,
        val shouldShowPermissionRationale: Boolean
    ): VoiceToTextEvent()
    data class ToggleRecording(val languageCode: String): VoiceToTextEvent()
    object Reset: VoiceToTextEvent()
}