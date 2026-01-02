package com.paligot.confily.backend.qanda.application

import com.paligot.confily.backend.events.infrastructure.firestore.EventFirestore
import com.paligot.confily.backend.qanda.domain.QAndARepository
import com.paligot.confily.backend.qanda.infrastructure.firestore.QAndAEntity
import com.paligot.confily.backend.qanda.infrastructure.firestore.QAndAFirestore
import com.paligot.confily.backend.qanda.infrastructure.firestore.convertToModel
import com.paligot.confily.models.QuestionAndResponse
import kotlinx.coroutines.coroutineScope

class QAndARepositoryDefault(
    private val eventDao: EventFirestore,
    private val qAndAFirestore: QAndAFirestore
) : QAndARepository {
    override suspend fun list(eventId: String, language: String): List<QuestionAndResponse> = coroutineScope {
        val qanda = qAndAFirestore.getAll(eventId, language)
            .map(QAndAEntity::convertToModel)
            .sortedBy { it.order }
        if (qanda.isEmpty()) {
            val event = eventDao.get(eventId)
            return@coroutineScope qAndAFirestore.getAll(eventId, event.defaultLanguage)
                .map(QAndAEntity::convertToModel)
                .sortedBy { it.order }
        }
        return@coroutineScope qanda
    }
}
