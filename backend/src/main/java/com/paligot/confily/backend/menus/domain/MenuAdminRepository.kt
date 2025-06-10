package com.paligot.confily.backend.menus.domain

import com.paligot.confily.models.inputs.LunchMenuInput

interface MenuAdminRepository {
    suspend fun update(eventId: String, menus: List<LunchMenuInput>): String
}
