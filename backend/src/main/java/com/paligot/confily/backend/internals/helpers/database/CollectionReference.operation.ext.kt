package com.paligot.confily.backend.internals.helpers.database

import com.google.cloud.firestore.CollectionReference
import com.google.cloud.firestore.SetOptions
import com.google.cloud.firestore.WriteResult
import kotlin.reflect.full.memberProperties

fun <T : Any> CollectionReference.upsert(childPath: String, item: T): WriteResult {
    return if (docExists(childPath)) {
        update(childPath, item)
    } else {
        insert(childPath, item)
    }
}

fun <T : Any> CollectionReference.insert(childPath: String, item: T): WriteResult =
    document(childPath).set(item).get()

fun <T : Any> CollectionReference.insert(transform: (id: String) -> T): String {
    val docRef = document()
    docRef.set(transform(docRef.id)).get()
    return docRef.id
}

fun <T : Any> CollectionReference.update(childPath: String, item: T): WriteResult {
    val map = item::class.memberProperties.associate { it.name to it.getter.call(item) }
    return document(childPath).set(map, SetOptions.merge()).get()
}

fun CollectionReference.delete(childPath: String): WriteResult = document(childPath).delete().get()
