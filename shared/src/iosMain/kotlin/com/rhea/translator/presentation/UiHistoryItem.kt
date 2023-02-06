package com.rhea.translator.presentation

data class UiHistoryItem(
    val id: Long,
    val fromLanguage: UILanguage,
    val toLanguage: UILanguage,
    val fromText: String,
    val toText: String
)