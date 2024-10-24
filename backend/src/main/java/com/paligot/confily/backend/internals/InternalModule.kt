package com.paligot.confily.backend.internals

import com.paligot.confily.backend.internals.GoogleServicesModule.cloudStorage
import com.paligot.confily.backend.internals.GoogleServicesModule.drive
import com.paligot.confily.backend.internals.GoogleServicesModule.secretManager
import com.paligot.confily.backend.internals.SystemEnv.gcpProjectId
import com.paligot.confily.backend.internals.SystemEnv.isCloud
import com.paligot.confily.backend.internals.SystemEnv.projectName
import com.paligot.confily.backend.internals.helpers.drive.DriveDataSource
import com.paligot.confily.backend.internals.helpers.image.TranscoderImage
import com.paligot.confily.backend.internals.helpers.secret.Secret
import com.paligot.confily.backend.internals.helpers.storage.Storage

object InternalModule {
    val driveDataSource: Lazy<DriveDataSource> = lazy {
        DriveDataSource.Factory.create(drive.value)
    }
    val secret: Lazy<Secret> = lazy {
        Secret.Factory.create(gcpProjectId, secretManager.value)
    }
    val storage: Lazy<Storage> = lazy {
        Storage.Factory.create(cloudStorage.value, projectName, isCloud)
    }
    val transcoder: Lazy<TranscoderImage> = lazy {
        TranscoderImage()
    }
    val commonApi: Lazy<CommonApi> = lazy {
        CommonApi.Factory.create(enableNetworkLogs = true)
    }
}
