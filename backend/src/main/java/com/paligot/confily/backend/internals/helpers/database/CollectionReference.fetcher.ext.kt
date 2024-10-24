package com.paligot.confily.backend.internals.helpers.database

import com.google.cloud.firestore.CollectionReference
import com.google.cloud.firestore.DocumentReference
import com.google.cloud.firestore.Query
import kotlin.reflect.KClass

fun CollectionReference.docExists(childPath: String): Boolean = document(childPath).exists()

fun CollectionReference.size(): Long = count().get().get().count

fun CollectionReference.isEmpty(): Boolean = size() == 0L

fun CollectionReference.isNotEmpty(): Boolean = !isEmpty()

inline fun <reified T> CollectionReference.getDocument(childPath: String): T? =
    document(childPath).getDocument<T>()

inline fun <reified T> CollectionReference.getDocuments(): List<T> =
    listDocuments().getDocuments<T>()

inline fun <reified T, reified R> CollectionReference.map(transform: (T) -> R): List<R> =
    listDocuments().map { transform(it.getDocument<T>()!!) }

fun CollectionReference.diffRefs(ids: List<String>): List<DocumentReference> =
    listDocuments().filter { it.id in ids }

inline fun <reified T : Any> CollectionReference.diff(ids: List<String>): List<T> =
    listDocuments().filter { it.id in ids }.getDocuments<T>()

inline fun <reified T : Any> CollectionReference.query(vararg ops: WhereOperation): List<T> =
    query(T::class, *ops)

fun <T : Any> CollectionReference.query(clazz: KClass<T>, vararg ops: WhereOperation): List<T> {
    var query: Query? = null
    ops.forEach {
        val requester = if (query == null) this else query!!
        query = when (it) {
            is WhereOperation.WhereEquals<*> -> requester.whereEqualTo(it.left, it.right)
            is WhereOperation.WhereNotEquals<*> -> requester.whereNotEqualTo(it.left, it.right)
            is WhereOperation.WhereIn<*> -> requester.whereIn(it.left, it.right)
            is WhereOperation.WhereNotIn<*> -> requester.whereNotIn(it.left, it.right)
        }
    }
    if (query == null) error("You can't create a query without any where condition")
    return query!!.get().get().documents.map { it.toObject(clazz.java) }
}
