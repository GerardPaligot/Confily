package com.paligot.confily.backend.third.parties.billetweb

import com.paligot.confily.backend.events.EventModule.eventDao

object BilletWebModule {
    val billetWebApi = lazy { BilletWebApi.Factory.create(enableNetworkLogs = true) }
    val billetWebRepository = lazy { BilletWebRepository(billetWebApi.value, eventDao.value) }
}
