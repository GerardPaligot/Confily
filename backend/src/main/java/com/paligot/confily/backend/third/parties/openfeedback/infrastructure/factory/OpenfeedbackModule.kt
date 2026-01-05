package com.paligot.confily.backend.third.parties.openfeedback.infrastructure.factory

import com.paligot.confily.backend.internals.infrastructure.exposed.PostgresModule
import com.paligot.confily.backend.third.parties.openfeedback.application.OpenfeedbackRepositoryExposed

object OpenfeedbackModule {
    val openfeedbackRepository by lazy {
        OpenfeedbackRepositoryExposed(PostgresModule.database)
    }
}
