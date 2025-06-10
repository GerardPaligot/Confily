package com.paligot.confily.backend.third.parties.billetweb

import com.paligot.confily.backend.internals.infrastructure.factory.FirestoreModule.eventFirestore

object BilletWebModule {
    val billetWebApi = lazy { BilletWebApi.Factory.create(enableNetworkLogs = true) }
    val billetWebRepository = lazy { BilletWebRepository(billetWebApi.value, eventFirestore.value) }
}
