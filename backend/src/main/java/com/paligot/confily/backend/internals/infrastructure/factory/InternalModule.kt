package com.paligot.confily.backend.internals.infrastructure.factory

import com.paligot.confily.backend.internals.helpers.drive.DriveDataSource
import com.paligot.confily.backend.internals.helpers.storage.BucketStorage
import com.paligot.confily.backend.internals.helpers.storage.Storage
import com.paligot.confily.backend.internals.helpers.storage.SupabaseStorage
import com.paligot.confily.backend.internals.infrastructure.provider.CommonApi
import com.paligot.confily.backend.internals.infrastructure.system.StorageProvider
import com.paligot.confily.backend.internals.infrastructure.system.SystemEnv
import com.paligot.confily.backend.internals.infrastructure.transcoder.TranscoderImage

object InternalModule {
    val driveDataSource by lazy {
        DriveDataSource.Factory.create(GoogleServicesModule.drive)
    }
    val storage: Storage by lazy {
        when (SystemEnv.storageProvider) {
            StorageProvider.SUPABASE -> SupabaseStorage(
                SupabaseServicesModule.client,
                SystemEnv.SupabaseProvider.storageBucket
                    ?: throw IllegalStateException("SUPABASE_STORAGE_BUCKET is required")
            )
            StorageProvider.GCP -> BucketStorage(
                GoogleServicesModule.cloudStorage,
                SystemEnv.GoogleProvider.storageBucket
                    ?: throw IllegalStateException("GOOGLE_STORAGE_BUCKET is required")
            )
        }
    }
    val transcoder by lazy {
        TranscoderImage()
    }
    val commonApi by lazy {
        CommonApi.Factory.create(enableNetworkLogs = true)
    }
}
