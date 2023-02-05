package com.rhea.translator.domain.local

import com.rhea.translator.database.TranslatorDatabase
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver

actual class DatabaseDriverFactory {
    actual fun create(): SqlDriver {
        return NativeSqliteDriver(TranslatorDatabase.Schema, "translate.db")
    }
}