package com.paligot.confily.backend.third.parties.welovedevs

object WeLoveDevsModule {
    val wldApi = lazy { WeLoveDevsApi.Factory.create(enableNetworkLogs = true) }
}
