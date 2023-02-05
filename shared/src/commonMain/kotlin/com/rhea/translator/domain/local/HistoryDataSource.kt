package com.rhea.translator.domain.local

import com.rhea.translator.domain.models.HistoryItem
import com.rhea.translator.domain.utils.CommonFlow

interface HistoryDataSource {
    fun getHistory(): CommonFlow<List<HistoryItem>>
    suspend fun insertHistory(item: HistoryItem)
}