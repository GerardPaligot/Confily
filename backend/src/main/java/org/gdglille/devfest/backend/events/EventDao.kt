package org.gdglille.devfest.backend.events

import org.gdglille.devfest.backend.NotAuthorized
import org.gdglille.devfest.backend.NotFoundException
import org.gdglille.devfest.backend.internals.helpers.database.BasicDatabase
import org.gdglille.devfest.backend.internals.helpers.database.get
import org.gdglille.devfest.backend.internals.helpers.database.getAll

class EventDao(
    private val projectName: String,
    private val database: BasicDatabase
) {
    suspend fun list(): List<EventDb> = database.getAll(projectName)
    suspend fun get(id: String): EventDb? = database.get(projectName, id)
    suspend fun getVerified(id: String, apiKey: String?): EventDb {
        val eventDb = database.get<EventDb>(projectName, id)
            ?: throw NotFoundException("Event $id Not Found")
        return if (eventDb.apiKey == apiKey) eventDb else throw NotAuthorized
    }

    suspend fun createOrUpdate(event: EventDb) {
        val existing = database.get<EventDb>(projectName, event.slugId)
        if (existing == null) database.insert(projectName, event.slugId, event)
        else database.update(projectName, event.slugId, event.copy(updatedAt = System.currentTimeMillis()))
    }

    suspend fun updateMenus(eventId: String, apiKey: String, menus: List<LunchMenuDb>) {
        val existing = getVerified(eventId, apiKey)
        database.update(projectName, eventId, existing.copy(menus = menus, updatedAt = System.currentTimeMillis()))
    }

    suspend fun updateQuestionsAndResponses(eventId: String, apiKey: String, qAndA: List<QuestionAndResponseDb>) {
        val existing = getVerified(eventId, apiKey)
        database.update(projectName, eventId, existing.copy(qanda = qAndA, updatedAt = System.currentTimeMillis()))
    }

    suspend fun updateCoc(eventId: String, apiKey: String, coc: String) {
        val existing = getVerified(eventId, apiKey)
        database.update(projectName, eventId, existing.copy(coc = coc, updatedAt = System.currentTimeMillis()))
    }

    suspend fun updateCategories(eventId: String, apiKey: String, categories: List<CategoryDb>) {
        val existing = getVerified(eventId, apiKey)
        database.update(
            projectName,
            eventId,
            existing.copy(categories = categories, updatedAt = System.currentTimeMillis())
        )
    }

    suspend fun updateFeatures(eventId: String, apiKey: String, hasNetworking: Boolean) {
        val existing = getVerified(eventId, apiKey)
        database.update(
            projectName,
            eventId,
            existing.copy(
                features = FeaturesActivatedDb(hasNetworking = hasNetworking),
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun updateUpdatedAt(event: EventDb) {
        database.update(projectName, event.slugId, event.copy(updatedAt = System.currentTimeMillis()))
    }

    suspend fun updateAgendaUpdatedAt(event: EventDb) {
        database.update(projectName, event.slugId, event.copy(agendaUpdatedAt = System.currentTimeMillis()))
    }
}
