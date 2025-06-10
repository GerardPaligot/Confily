package com.paligot.confily.backend.formats.infrastructure.factory

import com.paligot.confily.backend.formats.application.FormatAdminRepositoryDefault
import com.paligot.confily.backend.formats.application.FormatRepositoryDefault
import com.paligot.confily.backend.internals.infrastructure.factory.GoogleServicesModule
import com.paligot.confily.backend.internals.infrastructure.firestore.FormatFirestore
import com.paligot.confily.backend.internals.infrastructure.system.SystemEnv

object FormatModule {
    val formatFirestore = lazy { FormatFirestore(SystemEnv.projectName, GoogleServicesModule.cloudFirestore.value) }
    val formatRepository = lazy { FormatRepositoryDefault(formatFirestore.value) }
    val formatAdminRepository = lazy { FormatAdminRepositoryDefault(formatFirestore.value) }
}
