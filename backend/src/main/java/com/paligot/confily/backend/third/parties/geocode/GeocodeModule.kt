package com.paligot.confily.backend.third.parties.geocode

import com.paligot.confily.backend.internals.InternalModule.secret

object GeocodeModule {
    val geocodeApi = lazy { GeocodeApi.Factory.create(secret.value, enableNetworkLogs = true) }
}
