package com.paligot.confily.backend.internals.socials

import com.paligot.confily.models.SocialItem
import com.paligot.confily.models.inputs.SocialInput
import com.paligot.confily.models.mapToSocialType

fun SocialDb.convertToModel() = SocialItem(type = type.mapToSocialType(), url = url)

fun SocialInput.convertToDb() = SocialDb(type = type.name.lowercase(), url = url)
