package com.paligot.confily.backend.third.parties.billetweb.infrastructure.factory

import com.paligot.confily.backend.internals.infrastructure.exposed.PostgresModule
import com.paligot.confily.backend.third.parties.billetweb.application.BilletWebRepositoryExposed
import com.paligot.confily.backend.third.parties.billetweb.infrastructure.provider.BilletWebApi

object BilletWebModule {
    val billetWebApi by lazy { BilletWebApi.Factory.create(enableNetworkLogs = true) }
    val billetWebRepository by lazy {
        BilletWebRepositoryExposed(PostgresModule.database, billetWebApi)
    }
}
