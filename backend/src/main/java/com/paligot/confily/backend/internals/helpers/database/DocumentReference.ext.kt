package com.paligot.confily.backend.internals.helpers.database

import com.google.cloud.firestore.DocumentReference

fun DocumentReference.exists(): Boolean = get().get().exists()

inline fun <reified T> DocumentReference.getDocument(): T? = getDocument(T::class.java)

fun <T> DocumentReference.getDocument(clazz: Class<T>): T? = get().get().toObject(clazz)

inline fun <reified T> Iterable<DocumentReference>.getDocuments(): List<T> =
    map { it.getDocument<T>()!! }
