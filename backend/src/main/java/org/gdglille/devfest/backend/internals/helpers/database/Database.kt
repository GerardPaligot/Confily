package org.gdglille.devfest.backend.internals.helpers.database

import com.google.cloud.firestore.Firestore
import kotlin.reflect.KClass

@Suppress("TooManyFunctions")
interface Database {
    suspend fun count(eventId: String, collectionName: String): Long
    suspend fun <T : Any> get(eventId: String, collectionName: String, id: String, clazz: KClass<T>): T?
    suspend fun <T : Any> getAll(eventId: String, collectionName: String, clazz: KClass<T>): List<T>
    suspend fun <T : Any> query(
        eventId: String,
        collectionName: String,
        clazz: KClass<T>,
        vararg ops: WhereOperation
    ): List<T>

    suspend fun <T : Any> insert(eventId: String, collectionName: String, id: String, item: T)
    suspend fun <T : Any> insert(eventId: String, collectionName: String, transform: (id: String) -> T): String
    suspend fun <T : Any> update(eventId: String, collectionName: String, id: String, item: T)
    suspend fun delete(eventId: String, collectionName: String, id: String)
    suspend fun delete(eventId: String, collectionName: String)
    suspend fun deleteAll(eventId: String, collectionName: String, ids: List<String>)
    suspend fun diff(eventId: String, collectionName: String, ids: List<String>): List<String>
    suspend fun <T : Any> diff(eventId: String, collectionName: String, ids: List<String>, clazz: KClass<T>): List<T>

    object Factory {
        fun create(firestore: Firestore, projectName: String): Database = FirestoreDatabase(firestore, projectName)
    }
}

infix fun <R> String.whereEquals(that: R): WhereOperation.WhereEquals<R> =
    WhereOperation.WhereEquals(this, that)

infix fun <R> String.whereNotEquals(that: R): WhereOperation.WhereNotEquals<R> =
    WhereOperation.WhereNotEquals(this, that)

infix fun <R> String.whereIn(that: List<R>): WhereOperation.WhereIn<R> =
    WhereOperation.WhereIn(this, that)

infix fun <R> String.whereNotIn(that: List<R>): WhereOperation.WhereNotIn<R> =
    WhereOperation.WhereNotIn(this, that)

sealed class WhereOperation(val left: String) {
    class WhereEquals<R>(left: String, val right: R) : WhereOperation(left)
    class WhereNotEquals<R>(left: String, val right: R) : WhereOperation(left)
    class WhereIn<R>(left: String, val right: List<R>) : WhereOperation(left)
    class WhereNotIn<R>(left: String, val right: List<R>) : WhereOperation(left)
}

suspend inline fun <reified T : Any> Database.get(eventId: String, collectionName: String, id: String): T? =
    get(eventId, collectionName, id, T::class)

suspend inline fun <reified T : Any> Database.getAll(eventId: String, collectionName: String): List<T> =
    getAll(eventId, collectionName, T::class)

suspend inline fun <reified T : Any> Database.query(
    eventId: String,
    collectionName: String,
    vararg ops: WhereOperation
): List<T> = query(eventId, collectionName, T::class, *ops)

suspend inline fun <reified T : Any> Database.diff(
    eventId: String,
    collectionName: String,
    ids: List<String>
): List<T> = diff(eventId, collectionName, ids, T::class)
