package com.paligot.confily.backend.third.parties.geocode.infrastructure.factory

import com.paligot.confily.backend.internals.infrastructure.factory.InternalModule
import com.paligot.confily.backend.third.parties.geocode.infrastructure.provider.GeocodeApi

object GeocodeModule {
    val geocodeApi = lazy { GeocodeApi.Factory.create(InternalModule.secret.value, enableNetworkLogs = true) }
}
