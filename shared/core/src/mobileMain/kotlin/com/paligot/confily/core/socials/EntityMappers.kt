package com.paligot.confily.core.socials

import com.paligot.confily.core.socials.entities.Social

internal val socialMapper = { url: String, type: String -> Social(url, type) }
