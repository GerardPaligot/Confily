package com.paligot.confily.models.inputs

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TeamMemberInput(
    @SerialName("display_name")
    val displayName: String,
    val bio: String,
    val role: String? = null,
    @SerialName("photo_url")
    val photoUrl: String? = null,
    val socials: List<SocialInput> = emptyList()
) : Validator {
    override fun validate(): List<String> = emptyList()
}
