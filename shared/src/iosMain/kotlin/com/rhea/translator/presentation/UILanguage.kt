package com.rhea.translator.presentation

import com.rhea.translator.domain.models.Language

actual class UILanguage(
    actual val language: Language,
    val imageName: String
) {
    actual companion object {
        actual fun byCode(code: String): UILanguage {
            return allLanguages.find { it.language.langCode == code }
                ?: throw IllegalArgumentException("Invalid or unsupported language code")
        }

        actual val allLanguages: List<UILanguage>
            get() = Language.values().map { language ->
                UILanguage(
                    language = language,
                    imageName = language.langName.lowercase()
                )
            }
    }
}
