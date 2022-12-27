package org.gdglille.devfest.backend.internals.helpers.database

import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.Query
import com.google.cloud.firestore.SetOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties

class FirestoreBasicDatabase(private val firestore: Firestore) : BasicDatabase {
    override suspend fun count(collectionName: String): Long = withContext(Dispatchers.IO) {
        firestore.collection(collectionName).count().get().get().count
    }

    override suspend fun <T : Any> get(collectionName: String, id: String, clazz: KClass<T>): T? =
        withContext(Dispatchers.IO) {
            firestore
                .collection(collectionName)
                .document(id)
                .get()
                .get()
                .toObject(clazz.java)
        }

    override suspend fun <T : Any> getAll(collectionName: String, clazz: KClass<T>): List<T> =
        withContext(Dispatchers.IO) {
            firestore
                .collection(collectionName)
                .listDocuments()
                .map { it.get().get().toObject(clazz.java)!! }
        }

    override suspend fun <T : Any> query(
        collectionName: String,
        clazz: KClass<T>,
        vararg ops: WhereOperation
    ) = withContext(Dispatchers.IO) {
        val collRef = firestore.collection(collectionName)
        var query: Query? = null
        ops.forEach {
            val requester = if (query == null) collRef else query!!
            query = when (it) {
                is WhereOperation.WhereEquals<*> -> requester.whereEqualTo(it.left, it.right)
                is WhereOperation.WhereNotEquals<*> -> requester.whereNotEqualTo(it.left, it.right)
                is WhereOperation.WhereIn<*> -> requester.whereIn(it.left, it.right)
            }
        }
        if (query == null) error("You can't create a query without any where condition")
        query!!
            .get()
            .get()
            .documents
            .map { it.id to it.toObject(clazz.java) }
    }

    override suspend fun <T : Any> insert(collectionName: String, eventId: String, item: T) =
        withContext(Dispatchers.IO) {
            firestore
                .collection(collectionName)
                .document(eventId)
                .set(item)
                .get()
            Unit
        }

    override suspend fun <T : Any> update(collectionName: String, eventId: String, item: T) =
        withContext(Dispatchers.IO) {
            val map = item::class.memberProperties.associate { it.name to it.getter.call(item) }
            firestore
                .collection(collectionName)
                .document(eventId)
                .set(map, SetOptions.merge())
                .get()
            Unit
        }
}
