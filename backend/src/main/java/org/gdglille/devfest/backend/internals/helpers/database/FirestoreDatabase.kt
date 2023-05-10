package org.gdglille.devfest.backend.internals.helpers.database

import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.Query
import com.google.cloud.firestore.SetOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties

class FirestoreDatabase(private val firestore: Firestore, private val projectName: String) : Database {
    override suspend fun count(eventId: String, collectionName: String): Long = withContext(Dispatchers.IO) {
        firestore.collection(projectName).document(eventId).collection(collectionName).count().get().get().count
    }

    override suspend fun <T : Any> get(eventId: String, collectionName: String, id: String, clazz: KClass<T>): T? =
        withContext(Dispatchers.IO) {
            firestore
                .collection(projectName)
                .document(eventId)
                .collection(collectionName)
                .document(id)
                .get()
                .get()
                .toObject(clazz.java)
        }

    override suspend fun <T : Any> getAll(eventId: String, collectionName: String, clazz: KClass<T>): List<T> =
        withContext(Dispatchers.IO) {
            firestore
                .collection(projectName)
                .document(eventId)
                .collection(collectionName)
                .listDocuments()
                .map { it.get().get().toObject(clazz.java)!! }
        }

    override suspend fun <T : Any> query(
        eventId: String,
        collectionName: String,
        clazz: KClass<T>,
        vararg ops: WhereOperation
    ): List<T> =
        withContext(Dispatchers.IO) {
            val collRef = firestore
                .collection(projectName)
                .document(eventId)
                .collection(collectionName)
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
                .map { it.toObject(clazz.java) }
        }

    override suspend fun <T : Any> insert(eventId: String, collectionName: String, id: String, item: T) =
        withContext(Dispatchers.IO) {
            firestore
                .collection(projectName)
                .document(eventId)
                .collection(collectionName)
                .document(id)
                .set(item)
                .get()
            Unit
        }

    override suspend fun <T : Any> insert(
        eventId: String,
        collectionName: String,
        transform: (id: String) -> T
    ): String =
        withContext(Dispatchers.IO) {
            val docRef = firestore
                .collection(projectName)
                .document(eventId)
                .collection(collectionName)
                .document()
            val item = transform(docRef.id)
            docRef.set(item).get()
            return@withContext docRef.id
        }

    override suspend fun <T : Any> update(eventId: String, collectionName: String, id: String, item: T) =
        withContext(Dispatchers.IO) {
            val map = item::class.memberProperties.associate { it.name to it.getter.call(item) }
            firestore
                .collection(projectName)
                .document(eventId)
                .collection(collectionName)
                .document(id)
                .set(map, SetOptions.merge())
                .get()
            Unit
        }

    override suspend fun delete(eventId: String, collectionName: String, id: String) = withContext(Dispatchers.IO) {
        firestore
            .collection(projectName)
            .document(eventId)
            .collection(collectionName)
            .document(id)
            .delete()
            .get()
        Unit
    }

    override suspend fun delete(eventId: String, collectionName: String) = withContext(Dispatchers.IO) {
        val documents = firestore
            .collection(projectName)
            .document(eventId)
            .collection(collectionName)
            .listDocuments()
        documents.map { async { it.delete() } }.awaitAll()
        Unit
    }
}
