package com.rhea.translator.presentation

import com.rhea.translator.domain.models.Language

expect class UILanguage {
    val language: Language

    companion object {
        fun byCode(code: String): UILanguage
        val allLanguages: List<UILanguage>
    }
}