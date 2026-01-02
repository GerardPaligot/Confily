package com.paligot.confily.backend.speakers.infrastructure.exposed

import com.paligot.confily.backend.internals.infrastructure.exposed.SocialsTable
import com.paligot.confily.models.SocialItem
import com.paligot.confily.models.Speaker
import org.jetbrains.exposed.sql.selectAll

fun SpeakerEntity.toModel(): Speaker {
    val speakerId = this.id.value
    val socials = SpeakerSocialsTable
        .innerJoin(SocialsTable)
        .selectAll()
        .where { SpeakerSocialsTable.speakerId eq speakerId }
        .mapNotNull { row -> SocialItem(type = row[SocialsTable.platform], url = row[SocialsTable.url]) }
    return Speaker(
        id = speakerId.toString(),
        displayName = this.name,
        pronouns = this.pronouns,
        bio = this.bio ?: "",
        jobTitle = this.jobTitle,
        company = this.company,
        photoUrl = this.photoUrl ?: "",
        socials = socials
    )
}
