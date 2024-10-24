package com.paligot.confily.backend.internals.helpers.database

import com.google.cloud.firestore.DocumentReference
import com.google.cloud.firestore.Firestore

fun Firestore.batchDelete(docReferences: List<DocumentReference>) {
    val batch = batch()
    docReferences.forEach {
        batch.delete(it)
    }
    batch.commit().get()
}
