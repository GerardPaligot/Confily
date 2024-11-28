package com.paligot.confily.core.events.entities

import com.paligot.confily.models.ui.SocialTypeUi
import com.paligot.confily.models.ui.SocialUi
import kotlin.native.ObjCName

@ObjCName("SocialEntity")
class Social(
    val url: String,
    val type: String
)

fun Social.mapToUi(): SocialUi = SocialUi(
    url = url,
    type = SocialTypeUi.valueOf(type)
)
