package com.paligot.confily.backend.speakers.application

import com.paligot.confily.backend.internals.application.convertToEntity
import com.paligot.confily.backend.internals.application.convertToModel
import com.paligot.confily.backend.internals.infrastructure.firestore.SocialEntity
import com.paligot.confily.backend.internals.infrastructure.firestore.SpeakerEntity
import com.paligot.confily.models.SocialItem
import com.paligot.confily.models.SocialType
import com.paligot.confily.models.Speaker
import com.paligot.confily.models.inputs.SocialInput
import com.paligot.confily.models.inputs.SpeakerInput

fun SpeakerEntity.convertToModel(): Speaker = Speaker(
    id = this.id,
    displayName = this.displayName,
    pronouns = this.pronouns,
    bio = this.bio,
    jobTitle = this.jobTitle,
    company = this.company,
    photoUrl = this.photoUrl,
    socials = socials.map(SocialEntity::convertToModel).toMutableList().apply {
        if (find { it.type == SocialType.LinkedIn } == null && linkedin != null) {
            this.add(SocialItem(SocialType.LinkedIn, linkedin))
        }
        if (find { it.type == SocialType.X } == null && twitter != null) {
            this.add(SocialItem(SocialType.X, twitter))
        }
        if (find { it.type == SocialType.Mastodon } == null && mastodon != null) {
            this.add(SocialItem(SocialType.Mastodon, mastodon))
        }
        if (find { it.type == SocialType.Website } == null && website != null) {
            this.add(SocialItem(SocialType.Website, website))
        }
        if (find { it.type == SocialType.GitHub } == null && github != null) {
            this.add(SocialItem(SocialType.GitHub, github))
        }
    }.toList()
)

fun SpeakerInput.convertToEntity(photoUrl: String, id: String? = null) = SpeakerEntity(
    id = id ?: "",
    displayName = this.displayName,
    pronouns = this.pronouns,
    bio = this.bio,
    email = this.email,
    jobTitle = this.jobTitle,
    company = this.company,
    photoUrl = photoUrl,
    socials = socials.map(SocialInput::convertToEntity)
)
