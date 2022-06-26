package org.gdglille.devfest.backend.database

import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.Query
import com.google.cloud.firestore.SetOptions
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties

class FirestoreDatabase(
    private val firestore: Firestore,
    private val projectName: String,
    private val collectionName: String,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : Database {
    override suspend fun <T : Any> get(id: String, clazz: KClass<T>): T? = withContext(dispatcher) {
        firestore
            .collection(collectionName)
            .document(id)
            .get()
            .get()
            .toObject(clazz.java)
    }

    override suspend fun <T : Any> get(eventId: String, id: String, clazz: KClass<T>): T? = withContext(dispatcher) {
        firestore
            .collection(projectName)
            .document(eventId)
            .collection(collectionName)
            .document(id)
            .get()
            .get()
            .toObject(clazz.java)
    }

    override suspend fun <T : Any> getAll(eventId: String, clazz: KClass<T>): List<T> = withContext(dispatcher) {
        firestore
            .collection(projectName)
            .document(eventId)
            .collection(collectionName)
            .listDocuments()
            .map { it.get().get().toObject(clazz.java)!! }
    }

    override suspend fun <T : Any> query(clazz: KClass<T>, vararg ops: WhereOperation): List<T> =
        withContext(dispatcher) {
            val collRef = firestore
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

    override suspend fun <T : Any> query(eventId: String, clazz: KClass<T>, vararg ops: WhereOperation): List<T> =
        withContext(dispatcher) {
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

    override suspend fun <T : Any> insert(eventId: String, item: T) = withContext(dispatcher) {
        firestore
            .collection(collectionName)
            .document(eventId)
            .set(item)
            .get()
        Unit
    }

    override suspend fun <T : Any> insert(eventId: String, id: String, item: T) = withContext(dispatcher) {
        firestore
            .collection(projectName)
            .document(eventId)
            .collection(collectionName)
            .document(id)
            .set(item)
            .get()
        Unit
    }

    override suspend fun <T : Any> insert(eventId: String, transform: (id: String) -> T): String =
        withContext(dispatcher) {
            val docRef = firestore
                .collection(projectName)
                .document(eventId)
                .collection(collectionName)
                .document()
            val item = transform(docRef.id)
            docRef.set(item).get()
            return@withContext docRef.id
        }

    override suspend fun <T : Any> update(eventId: String, item: T) = withContext(dispatcher) {
        val map = item::class.memberProperties.associate { it.name to it.getter.call(item) }
        firestore
            .collection(collectionName)
            .document(eventId)
            .set(map, SetOptions.merge())
            .get()
        Unit
    }

    override suspend fun <T : Any> update(eventId: String, id: String, item: T) = withContext(dispatcher) {
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

    override suspend fun delete(eventId: String, id: String) = withContext(dispatcher) {
        firestore
            .collection(projectName)
            .document(eventId)
            .collection(collectionName)
            .document(id)
            .delete()
            .get()
        Unit
    }
}
