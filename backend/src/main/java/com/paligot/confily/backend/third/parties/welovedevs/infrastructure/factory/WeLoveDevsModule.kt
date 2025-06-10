package com.paligot.confily.backend.third.parties.welovedevs.infrastructure.factory

import com.paligot.confily.backend.third.parties.welovedevs.infrastructure.provider.WeLoveDevsApi

object WeLoveDevsModule {
    val wldApi = lazy { WeLoveDevsApi.Factory.create(enableNetworkLogs = true) }
}
