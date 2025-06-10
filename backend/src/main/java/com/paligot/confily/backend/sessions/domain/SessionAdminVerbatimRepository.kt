package com.paligot.confily.backend.sessions.domain

import com.paligot.confily.models.inputs.TalkVerbatimInput

interface SessionAdminVerbatimRepository {
    suspend fun create(eventId: String, verbatim: TalkVerbatimInput): List<String>
    suspend fun create(eventId: String, talkId: String, verbatim: TalkVerbatimInput): String
    suspend fun grantPermissions(eventId: String, verbatim: TalkVerbatimInput)
    suspend fun grantPermissionByTalk(eventId: String, talkId: String, verbatim: TalkVerbatimInput): String
}
