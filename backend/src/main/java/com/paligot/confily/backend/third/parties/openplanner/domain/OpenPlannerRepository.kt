package com.paligot.confily.backend.third.parties.openplanner.domain

interface OpenPlannerRepository {
    suspend fun update(eventId: String)
}
