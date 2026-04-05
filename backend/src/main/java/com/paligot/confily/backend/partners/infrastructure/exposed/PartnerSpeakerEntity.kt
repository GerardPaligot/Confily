package com.paligot.confily.backend.partners.infrastructure.exposed

import com.paligot.confily.backend.speakers.infrastructure.exposed.SpeakerEntity
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.UUID

class PartnerSpeakerEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<PartnerSpeakerEntity>(PartnerSpeakersTable) {
        fun findByPartner(partnerId: UUID): List<PartnerSpeakerEntity> = find {
            PartnerSpeakersTable.partnerId eq partnerId
        }.toList()

        fun findBySpeaker(speakerId: UUID): List<PartnerSpeakerEntity> = find {
            PartnerSpeakersTable.speakerId eq speakerId
        }.toList()
    }

    var partner by PartnerEntity referencedOn PartnerSpeakersTable.partnerId
    var speaker by SpeakerEntity referencedOn PartnerSpeakersTable.speakerId
}
