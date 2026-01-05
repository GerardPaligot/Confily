package com.paligot.confily.backend.internals.infrastructure.factory

import com.paligot.confily.backend.internals.helpers.drive.DriveDataSource
import com.paligot.confily.backend.internals.helpers.storage.BucketStorage
import com.paligot.confily.backend.internals.infrastructure.provider.CommonApi
import com.paligot.confily.backend.internals.infrastructure.system.SystemEnv
import com.paligot.confily.backend.internals.infrastructure.transcoder.TranscoderImage

object InternalModule {
    val driveDataSource by lazy {
        DriveDataSource.Factory.create(GoogleServicesModule.drive)
    }
    val storage by lazy {
        BucketStorage(
            GoogleServicesModule.cloudStorage,
            SystemEnv.GoogleProvider.storageBucket
        )
    }
    val transcoder by lazy {
        TranscoderImage()
    }
    val commonApi by lazy {
        CommonApi.Factory.create(enableNetworkLogs = true)
    }
}
