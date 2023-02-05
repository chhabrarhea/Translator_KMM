package com.rhea.translator.domain.local

import com.rhea.translator.database.TranslatorDatabase
import com.rhea.translator.domain.models.HistoryItem
import com.rhea.translator.domain.models.mappers.toHistoryItem
import com.rhea.translator.domain.utils.CommonFlow
import com.rhea.translator.domain.utils.toCommonFlow
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock

class SqlDelightHistoryDataSource (db: TranslatorDatabase): HistoryDataSource {

    private val queries = db.translateQueries

    override fun getHistory(): CommonFlow<List<HistoryItem>> {
        return queries
            .getHistory()
            .asFlow()
            .mapToList()
            .map { it ->
                it.map { it.toHistoryItem() }
            }
            .toCommonFlow()
    }

    override suspend fun insertHistory(item: HistoryItem) {
        queries.insertHistoryEntity(
            item.id ?: 0L,
            item.fromLanguageCode,
            item.fromText,
            item.toLanguageCode,
            item.toText,
            Clock.System.now().epochSeconds
        )
    }
}