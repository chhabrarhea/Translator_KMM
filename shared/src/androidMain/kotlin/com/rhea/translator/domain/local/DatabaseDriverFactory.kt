package com.rhea.translator.domain.local

import android.content.Context
import com.rhea.translator.database.TranslatorDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver

actual class DatabaseDriverFactory(private val context: Context) {
    actual fun create(): SqlDriver {
        return AndroidSqliteDriver(TranslatorDatabase.Schema, context, "translate.db")
    }
}