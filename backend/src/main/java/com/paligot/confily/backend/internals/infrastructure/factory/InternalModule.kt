package com.paligot.confily.backend.internals.infrastructure.factory

import com.paligot.confily.backend.internals.helpers.drive.DriveDataSource
import com.paligot.confily.backend.internals.helpers.image.TranscoderImage
import com.paligot.confily.backend.internals.helpers.secret.Secret
import com.paligot.confily.backend.internals.helpers.storage.Storage
import com.paligot.confily.backend.internals.infrastructure.provider.CommonApi
import com.paligot.confily.backend.internals.infrastructure.system.SystemEnv

object InternalModule {
    val driveDataSource: Lazy<DriveDataSource> = lazy {
        DriveDataSource.Factory.create(GoogleServicesModule.drive.value)
    }
    val secret: Lazy<Secret> = lazy {
        Secret.Factory.create(SystemEnv.gcpProjectId, GoogleServicesModule.secretManager.value)
    }
    val storage: Lazy<Storage> = lazy {
        Storage.Factory.create(GoogleServicesModule.cloudStorage.value, SystemEnv.projectName, SystemEnv.isCloud)
    }
    val transcoder: Lazy<TranscoderImage> = lazy {
        TranscoderImage()
    }
    val commonApi: Lazy<CommonApi> = lazy {
        CommonApi.Factory.create(enableNetworkLogs = true)
    }
}
