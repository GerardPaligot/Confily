package com.paligot.confily.backend.partners.infrastructure.exposed

import com.paligot.confily.backend.speakers.infrastructure.exposed.SpeakersTable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.selectAll
import java.util.UUID

object PartnerSpeakersTable : UUIDTable("partner_speakers") {
    val partnerId = reference("partner_id", PartnersTable, onDelete = ReferenceOption.CASCADE)
    val speakerId = reference("speaker_id", SpeakersTable, onDelete = ReferenceOption.CASCADE)

    init {
        index(isUnique = false, partnerId)
        index(isUnique = false, speakerId)
        uniqueIndex(partnerId, speakerId)
    }

    fun speakerIds(partnerId: UUID): List<UUID> = this.selectAll()
        .where { PartnerSpeakersTable.partnerId eq partnerId }
        .map { it[speakerId].value }

    fun partnerIds(speakerId: UUID): List<UUID> = this.selectAll()
        .where { PartnerSpeakersTable.speakerId eq speakerId }
        .map { it[partnerId].value }
}
