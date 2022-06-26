package org.gdglille.devfest.backend.database

import com.google.cloud.firestore.Firestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlin.reflect.KClass

sealed class DatabaseType {
    class FirestoreDb(
        val firestore: Firestore,
        val projectName: String,
        val collectionName: String,
        val dispatcher: CoroutineDispatcher = Dispatchers.Default
    ) : DatabaseType()
}

interface Database {
    suspend fun <T : Any> get(id: String, clazz: KClass<T>): T?
    suspend fun <T : Any> query(clazz: KClass<T>, vararg ops: WhereOperation): List<T>
    suspend fun <T : Any> insert(eventId: String, item: T)
    suspend fun <T : Any> update(eventId: String, item: T)

    suspend fun <T : Any> get(eventId: String, id: String, clazz: KClass<T>): T?
    suspend fun <T : Any> getAll(eventId: String, clazz: KClass<T>): List<T>
    suspend fun <T : Any> query(
        eventId: String,
        clazz: KClass<T>,
        vararg ops: WhereOperation
    ): List<T>
    suspend fun <T : Any> insert(eventId: String, id: String, item: T)
    suspend fun <T : Any> insert(eventId: String, transform: (id: String) -> T): String
    suspend fun <T : Any> update(eventId: String, id: String, item: T)
    suspend fun delete(eventId: String, id: String)

    object Factory {
        fun create(type: DatabaseType): Database = when (type) {
            is DatabaseType.FirestoreDb -> FirestoreDatabase(
                type.firestore, type.projectName, type.collectionName, type.dispatcher
            )
        }
    }
}

infix fun <R> String.whereEquals(that: R): WhereOperation.WhereEquals<R> =
    WhereOperation.WhereEquals(this, that)

infix fun <R> String.whereNotEquals(that: R): WhereOperation.WhereNotEquals<R> =
    WhereOperation.WhereNotEquals(this, that)

infix fun <R> String.whereIn(that: List<R>): WhereOperation.WhereIn<R> =
    WhereOperation.WhereIn(this, that)

sealed class WhereOperation(val left: String) {
    class WhereEquals<R>(left: String, val right: R) : WhereOperation(left)
    class WhereNotEquals<R>(left: String, val right: R) : WhereOperation(left)
    class WhereIn<R>(left: String, val right: List<R>) : WhereOperation(left)
}

suspend inline fun <reified T : Any> Database.get(id: String): T? = get(id, T::class)
suspend inline fun <reified T : Any> Database.get(eventId: String, id: String): T? =
    get(eventId, id, T::class)

suspend inline fun <reified T : Any> Database.getAll(eventId: String): List<T> =
    getAll(eventId, T::class)

suspend inline fun <reified T : Any> Database.query(vararg ops: WhereOperation): List<T> =
    query(T::class, *ops)

suspend inline fun <reified T : Any> Database.query(
    eventId: String,
    vararg ops: WhereOperation
): List<T> =
    query(eventId, T::class, *ops)
