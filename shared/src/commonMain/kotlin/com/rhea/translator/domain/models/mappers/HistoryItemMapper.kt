package com.rhea.translator.domain.models.mappers

import com.rhea.translator.domain.models.HistoryItem
import comrheatranslatedatabase.HistoryEntity


fun HistoryEntity.toHistoryItem(): HistoryItem {
    return HistoryItem(
        id = id,
        fromLanguageCode = fromLanguageCode,
        fromText = fromText,
        toLanguageCode = toLanguageCode,
        toText = toText
    )
}