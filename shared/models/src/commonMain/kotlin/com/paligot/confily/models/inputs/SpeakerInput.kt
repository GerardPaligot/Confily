package com.paligot.confily.models.inputs

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SpeakerInput(
    @SerialName("display_name")
    val displayName: String,
    val pronouns: String? = null,
    val bio: String,
    val email: String? = null,
    @SerialName("job_title")
    val jobTitle: String? = null,
    val company: String? = null,
    @SerialName("photo_url")
    val photoUrl: String,
    val socials: List<SocialInput> = emptyList()
) : Validator {
    override fun validate(): List<String> = arrayListOf<String>().apply {
        if (pronouns?.contains("/") == false) {
            add("Pronounce should contain separator '/'")
        }
    }
}
