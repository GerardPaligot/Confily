package com.paligot.confily.backend.events.application

import com.paligot.confily.backend.internals.application.convertToEntity
import com.paligot.confily.backend.internals.infrastructure.firestore.AddressEntity
import com.paligot.confily.backend.internals.infrastructure.firestore.BilletWebConfigurationEntity
import com.paligot.confily.backend.internals.infrastructure.firestore.EventEntity
import com.paligot.confily.backend.internals.infrastructure.firestore.OpenPlannerConfigurationEntity
import com.paligot.confily.backend.internals.infrastructure.firestore.TeamGroupEntity
import com.paligot.confily.backend.internals.infrastructure.firestore.WldConfigurationEntity
import com.paligot.confily.models.inputs.BilletWebConfigInput
import com.paligot.confily.models.inputs.EventInput
import com.paligot.confily.models.inputs.OpenPlannerConfigInput
import com.paligot.confily.models.inputs.SocialInput
import com.paligot.confily.models.inputs.TeamGroupInput
import com.paligot.confily.models.inputs.WldConfigInput

fun EventEntity.convertToEntity(input: EventInput, addressEntity: AddressEntity?): EventEntity = copy(
    name = input.name ?: this.name,
    startDate = input.startDate ?: this.startDate,
    endDate = input.endDate ?: this.endDate,
    sponsoringTypes = input.sponsoringTypes ?: this.sponsoringTypes,
    contactPhone = input.contactPhone ?: this.contactPhone,
    contactEmail = input.contactEmail ?: this.contactEmail,
    address = addressEntity ?: this.address,
    openFeedbackId = input.openFeedbackId ?: this.openFeedbackId,
    openPlannerConfig = input.openPlannerConfigInput?.convertToEntity() ?: this.openPlannerConfig,
    billetWebConfig = input.billetWebConfig?.convertToEntity() ?: this.billetWebConfig,
    wldConfig = input.wldConfig?.convertToEntity() ?: this.wldConfig,
    eventSessionTracks = eventSessionTracks,
    teamGroups = input.teamGroups?.map(TeamGroupInput::convertToEntity) ?: this.teamGroups,
    socials = input.socials?.map(SocialInput::convertToEntity) ?: this.socials,
    faqLink = input.faqLink ?: this.faqLink,
    codeOfConductLink = input.codeOfConductLink ?: this.codeOfConductLink,
    published = published,
    eventUpdatedAt = System.currentTimeMillis(),
    updatedAt = this.updatedAt
)

private fun OpenPlannerConfigInput.convertToEntity(): OpenPlannerConfigurationEntity =
    OpenPlannerConfigurationEntity(eventId = eventId, privateId = privateId)

private fun WldConfigInput.convertToEntity(): WldConfigurationEntity =
    WldConfigurationEntity(appId = appId, apiKey = apiKey)

private fun BilletWebConfigInput.convertToEntity(): BilletWebConfigurationEntity =
    BilletWebConfigurationEntity(eventId = eventId, userId = userId, apiKey = apiKey)

private fun TeamGroupInput.convertToEntity(): TeamGroupEntity = TeamGroupEntity(name = name, order = order)
