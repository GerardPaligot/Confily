package org.gdglille.devfest.backend.events

import org.gdglille.devfest.backend.NotAuthorized
import org.gdglille.devfest.backend.NotFoundException
import org.gdglille.devfest.backend.database.Database
import org.gdglille.devfest.backend.database.get
import org.gdglille.devfest.models.inputs.CategoryInput

class EventDao(private val database: Database) {
    suspend fun get(id: String): EventDb? = database.get(id)
    suspend fun getVerified(id: String, apiKey: String?): EventDb {
        val eventDb = database.get<EventDb>(id) ?: throw NotFoundException("Event $id Not Found")
        return if (eventDb.apiKey == apiKey) eventDb else throw NotAuthorized
    }

    suspend fun createOrUpdate(event: EventDb) {
        val existing = database.get<EventDb>(event.year)
        if (existing == null) database.insert(event.year, event)
        else database.update(event.year, event)
    }

    suspend fun updateMenus(eventId: String, apiKey: String, menus: List<LunchMenuDb>) {
        val existing = getVerified(eventId, apiKey)
        database.update(eventId, existing.copy(menus = menus))
    }

    suspend fun updateQuestionsAndResponses(eventId: String, apiKey: String, qAndA: List<QuestionAndResponseDb>) {
        val existing = getVerified(eventId, apiKey)
        database.update(eventId, existing.copy(qanda = qAndA))
    }

    suspend fun updateCoc(eventId: String, apiKey: String, coc: String) {
        val existing = getVerified(eventId, apiKey)
        database.update(eventId, existing.copy(coc = coc))
    }

    suspend fun updateCategories(eventId: String, apiKey: String, categories: List<CategoryDb>) {
        val existing = getVerified(eventId, apiKey)
        database.update(eventId, existing.copy(categories = categories))
    }

    suspend fun updateFeatures(eventId: String, apiKey: String, hasNetworking: Boolean) {
        val existing = getVerified(eventId, apiKey)
        database.update(eventId, existing.copy(features = FeaturesActivatedDb(hasNetworking = hasNetworking)))
    }

    suspend fun updateUpdatedAt(id: String) {
        val existing = database.get<EventDb>(id) ?: return
        database.update(id, existing.copy(updatedAt = System.currentTimeMillis()))
    }
}
