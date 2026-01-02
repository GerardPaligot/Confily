package com.paligot.confily.backend.third.parties.openfeedback.infrastructure.factory

import com.paligot.confily.backend.internals.infrastructure.exposed.PostgresModule
import com.paligot.confily.backend.internals.infrastructure.factory.FirestoreModule
import com.paligot.confily.backend.internals.infrastructure.storage.StorageModule
import com.paligot.confily.backend.internals.infrastructure.system.SystemEnv
import com.paligot.confily.backend.third.parties.openfeedback.application.OpenfeedbackRepositoryDefault
import com.paligot.confily.backend.third.parties.openfeedback.application.OpenfeedbackRepositoryExposed

object OpenfeedbackModule {
    val openfeedbackRepository = lazy {
        if (SystemEnv.DatabaseConfig.hasPostgres) {
            OpenfeedbackRepositoryExposed(PostgresModule.database)
        } else {
            OpenfeedbackRepositoryDefault(
                eventFirestore = FirestoreModule.eventFirestore.value,
                eventStorage = StorageModule.eventStorage.value
            )
        }
    }
}
