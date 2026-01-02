package com.paligot.confily.backend.third.parties.openfeedback.infrastructure.factory

import com.paligot.confily.backend.internals.infrastructure.factory.FirestoreModule
import com.paligot.confily.backend.internals.infrastructure.storage.StorageModule
import com.paligot.confily.backend.third.parties.openfeedback.application.OpenfeedbackRepositoryDefault

object OpenfeedbackModule {
    val openfeedbackRepository = lazy {
        OpenfeedbackRepositoryDefault(
            eventFirestore = FirestoreModule.eventFirestore.value,
            eventStorage = StorageModule.eventStorage.value
        )
    }
}
