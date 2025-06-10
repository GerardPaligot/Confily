package com.paligot.confily.backend.map.infrastructure.factory

import com.paligot.confily.backend.internals.infrastructure.factory.GoogleServicesModule
import com.paligot.confily.backend.internals.infrastructure.factory.InternalModule
import com.paligot.confily.backend.internals.infrastructure.firestore.MapFirestore
import com.paligot.confily.backend.internals.infrastructure.storage.MapStorage
import com.paligot.confily.backend.internals.infrastructure.system.SystemEnv
import com.paligot.confily.backend.map.application.MapAdminRepositoryDefault
import com.paligot.confily.backend.map.application.MapRepositoryDefault

object MapModule {
    val mapFirestore = lazy {
        MapFirestore(
            SystemEnv.projectName,
            GoogleServicesModule.cloudFirestore.value,
            InternalModule.storage.value
        )
    }
    val mapStorage = lazy { MapStorage(InternalModule.storage.value) }
    val mapRepository = lazy { MapRepositoryDefault(mapFirestore.value) }
    val mapAdminRepository = lazy { MapAdminRepositoryDefault(mapFirestore.value, mapStorage.value) }
}
