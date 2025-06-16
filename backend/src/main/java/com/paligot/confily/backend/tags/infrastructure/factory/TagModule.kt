package com.paligot.confily.backend.tags.infrastructure.factory

import com.paligot.confily.backend.internals.infrastructure.factory.GoogleServicesModule
import com.paligot.confily.backend.internals.infrastructure.firestore.TagFirestore
import com.paligot.confily.backend.internals.infrastructure.system.SystemEnv
import com.paligot.confily.backend.tags.application.TagAdminRepositoryDefault
import com.paligot.confily.backend.tags.application.TagRepositoryDefault

object TagModule {
    val tagFirestore = lazy { TagFirestore(SystemEnv.projectName, GoogleServicesModule.cloudFirestore.value) }
    val tagRepository = lazy { TagRepositoryDefault(tagFirestore.value) }
    val tagAdminRepository = lazy { TagAdminRepositoryDefault(tagFirestore.value) }
}
