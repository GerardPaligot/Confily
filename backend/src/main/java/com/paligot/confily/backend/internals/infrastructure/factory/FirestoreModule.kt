package com.paligot.confily.backend.internals.infrastructure.factory

import com.paligot.confily.backend.internals.infrastructure.factory.GoogleServicesModule.cloudFirestore
import com.paligot.confily.backend.internals.infrastructure.firestore.CategoryFirestore
import com.paligot.confily.backend.internals.infrastructure.firestore.EventFirestore
import com.paligot.confily.backend.internals.infrastructure.system.SystemEnv.projectName

object FirestoreModule {
    val categoryFirestore = lazy { CategoryFirestore(projectName, cloudFirestore.value) }
    val eventFirestore = lazy { EventFirestore(projectName, cloudFirestore.value) }
}
