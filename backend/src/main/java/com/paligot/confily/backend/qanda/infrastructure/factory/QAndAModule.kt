package com.paligot.confily.backend.qanda.infrastructure.factory

import com.paligot.confily.backend.internals.infrastructure.factory.FirestoreModule
import com.paligot.confily.backend.internals.infrastructure.factory.GoogleServicesModule
import com.paligot.confily.backend.internals.infrastructure.firestore.QAndAFirestore
import com.paligot.confily.backend.internals.infrastructure.system.SystemEnv
import com.paligot.confily.backend.qanda.application.QAndAAdminRepositoryDefault
import com.paligot.confily.backend.qanda.application.QAndARepositoryDefault

object QAndAModule {
    val qAndAFirestore = lazy { QAndAFirestore(SystemEnv.projectName, GoogleServicesModule.cloudFirestore.value) }
    val qAndAAdminRepository =
        lazy { QAndAAdminRepositoryDefault(qAndAFirestore.value) }
    val qAndARepository = lazy { QAndARepositoryDefault(FirestoreModule.eventFirestore.value, qAndAFirestore.value) }
}
