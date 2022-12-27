package org.gdglille.devfest.backend.internals.helpers.database

import com.google.cloud.firestore.Firestore
import kotlin.reflect.KClass

interface BasicDatabase {
    suspend fun count(collectionName: String): Long
    suspend fun <T : Any> get(collectionName: String, id: String, clazz: KClass<T>): T?
    suspend fun <T : Any> getAll(collectionName: String, clazz: KClass<T>): List<T>
    suspend fun <T : Any> query(collectionName: String, clazz: KClass<T>, vararg ops: WhereOperation):
        List<Pair<String, T>>

    suspend fun <T : Any> insert(collectionName: String, eventId: String, item: T)
    suspend fun <T : Any> update(collectionName: String, eventId: String, item: T)

    object Factory {
        fun create(firestore: Firestore): BasicDatabase = FirestoreBasicDatabase(firestore)
    }
}

suspend inline fun <reified T : Any> BasicDatabase.getAll(collectionName: String): List<T> =
    getAll(collectionName, T::class)
suspend inline fun <reified T : Any> BasicDatabase.get(collectionName: String, id: String): T? =
    get(collectionName, id, T::class)
