package com.paligot.confily.core.schedules.entities

import kotlin.native.ObjCName

@ObjCName("FeedbackConfigEntity")
class FeedbackConfig(
    val projectId: String,
    val sessionId: String,
    val url: String
)
