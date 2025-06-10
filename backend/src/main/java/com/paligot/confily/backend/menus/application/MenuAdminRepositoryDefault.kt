package com.paligot.confily.backend.menus.application

import com.paligot.confily.backend.internals.infrastructure.firestore.EventFirestore
import com.paligot.confily.backend.menus.domain.MenuAdminRepository
import com.paligot.confily.models.inputs.LunchMenuInput

class MenuAdminRepositoryDefault(
    private val eventFirestore: EventFirestore
) : MenuAdminRepository {
    override suspend fun update(eventId: String, menus: List<LunchMenuInput>): String {
        eventFirestore.updateMenus(eventId, menus.map(LunchMenuInput::convertToEntity))
        return eventId
    }
}
