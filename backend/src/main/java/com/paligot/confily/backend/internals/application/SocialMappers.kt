package com.paligot.confily.backend.internals.application

import com.paligot.confily.backend.internals.infrastructure.firestore.SocialEntity
import com.paligot.confily.models.SocialItem
import com.paligot.confily.models.inputs.SocialInput
import com.paligot.confily.models.mapToSocialType

fun SocialEntity.convertToModel() = SocialItem(type = type.mapToSocialType(), url = url)

fun SocialInput.convertToEntity() = SocialEntity(type = type.name.lowercase(), url = url)
