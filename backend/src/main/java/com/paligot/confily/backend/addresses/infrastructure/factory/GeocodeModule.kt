package com.paligot.confily.backend.addresses.infrastructure.factory

import com.paligot.confily.backend.addresses.infrastructure.provider.GeocodeApi
import com.paligot.confily.backend.internals.infrastructure.system.SystemEnv

object GeocodeModule {
    val geocodeApi by lazy {
        GeocodeApi.Factory.create(SystemEnv.GoogleProvider.geocodeApiKey, enableNetworkLogs = true)
    }
}
