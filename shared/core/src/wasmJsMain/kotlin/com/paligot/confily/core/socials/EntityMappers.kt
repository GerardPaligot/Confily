package com.paligot.confily.core.socials

import com.paligot.confily.core.socials.entities.Social

fun SocialDb.mapToEntity(): Social = Social(url = url, type = type)
