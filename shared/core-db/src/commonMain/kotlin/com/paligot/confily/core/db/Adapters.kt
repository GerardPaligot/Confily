package com.paligot.confily.core.db

import app.cash.sqldelight.ColumnAdapter

val listOfStringsAdapter = object : ColumnAdapter<List<String>, String> {
    override fun decode(databaseValue: String) =
        if (databaseValue.isEmpty()) listOf() else databaseValue.split(", ")

    override fun encode(value: List<String>) = value.joinToString(separator = ", ")
}
