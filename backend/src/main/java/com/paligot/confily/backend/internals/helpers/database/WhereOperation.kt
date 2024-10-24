package com.paligot.confily.backend.internals.helpers.database

sealed class WhereOperation(val left: String) {
    class WhereEquals<R>(left: String, val right: R) : WhereOperation(left)
    class WhereNotEquals<R>(left: String, val right: R) : WhereOperation(left)
    class WhereIn<R>(left: String, val right: List<R>) : WhereOperation(left)
    class WhereNotIn<R>(left: String, val right: List<R>) : WhereOperation(left)
}

infix fun <R> String.whereEquals(that: R): WhereOperation.WhereEquals<R> =
    WhereOperation.WhereEquals(this, that)

infix fun <R> String.whereNotEquals(that: R): WhereOperation.WhereNotEquals<R> =
    WhereOperation.WhereNotEquals(this, that)

infix fun <R> String.whereIn(that: List<R>): WhereOperation.WhereIn<R> =
    WhereOperation.WhereIn(this, that)

infix fun <R> String.whereNotIn(that: List<R>): WhereOperation.WhereNotIn<R> =
    WhereOperation.WhereNotIn(this, that)
