package com.paligot.confily.backend.third.parties.billetweb.infrastructure.factory

import com.paligot.confily.backend.internals.infrastructure.exposed.PostgresModule
import com.paligot.confily.backend.internals.infrastructure.factory.FirestoreModule
import com.paligot.confily.backend.internals.infrastructure.system.SystemEnv
import com.paligot.confily.backend.third.parties.billetweb.application.BilletWebRepositoryDefault
import com.paligot.confily.backend.third.parties.billetweb.application.BilletWebRepositoryExposed
import com.paligot.confily.backend.third.parties.billetweb.infrastructure.provider.BilletWebApi

object BilletWebModule {
    val billetWebApi = lazy { BilletWebApi.Factory.create(enableNetworkLogs = true) }
    val billetWebRepository = lazy {
        if (SystemEnv.DatabaseConfig.hasPostgres) {
            BilletWebRepositoryExposed(PostgresModule.database, billetWebApi.value)
        } else {
            BilletWebRepositoryDefault(billetWebApi.value, FirestoreModule.eventFirestore.value)
        }
    }
}
