package com.paligot.confily.core.events

import com.paligot.confily.core.combineAllSerializableScopedFlow
import com.paligot.confily.core.events.QAndAQueries.Scopes.qanda
import com.paligot.confily.core.events.QAndAQueries.Scopes.qandaaction
import com.paligot.confily.core.getAllSerializableScopedFlow
import com.paligot.confily.core.putSerializableScoped
import com.russhwolf.settings.ObservableSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class QAndAQueries(private val settings: ObservableSettings) {
    private object Scopes {
        const val qanda = "qanda"
        const val qandaaction = "qandaaction"
    }

    fun insertQAndA(qAndA: QAndADb) {
        settings.putSerializableScoped(qanda, qAndA.order.toString(), qAndA)
    }

    fun selectQAndA(eventId: String): Flow<List<QAndADb>> = combine(
        flows = settings.getAllSerializableScopedFlow<QAndADb>(qanda),
        transform = { qAndAs ->
            qAndAs
                .filter { it.eventId == eventId }
                .sortedBy { it.order }
        }
    )

    fun insertQAndAAction(qAndAAction: QAndAActionDb) {
        settings.putSerializableScoped(qandaaction, qAndAAction.id, qAndAAction)
    }

    fun selectQAndAActions(eventId: String): Flow<List<QAndAActionDb>> =
        settings.combineAllSerializableScopedFlow(qandaaction) { it.eventId == eventId }
}
