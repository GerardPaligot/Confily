package com.paligot.confily.core.events.entities

import kotlin.native.ObjCName

@ObjCName("FeatureFlagsEntity")
class FeatureFlags(
    val hasSpeakerList: Boolean,
    val hasNetworking: Boolean,
    val hasPartnerList: Boolean,
    val hasMenus: Boolean,
    val hasQAndA: Boolean,
    val hasTicketIntegration: Boolean,
    val hasTeamMembers: Boolean
)
