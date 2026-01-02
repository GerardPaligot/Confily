package com.paligot.confily.backend.partners.infrastructure.factory

import com.paligot.confily.backend.partners.infrastructure.provider.WeLoveDevsApi

object WeLoveDevsModule {
    val wldApi = lazy { WeLoveDevsApi.Factory.create(enableNetworkLogs = true) }
}
