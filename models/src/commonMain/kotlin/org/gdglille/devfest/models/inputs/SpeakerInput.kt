package org.gdglille.devfest.models.inputs

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SpeakerInput(
    @SerialName("display_name")
    val displayName: String,
    val pronouns: String? = null,
    val bio: String,
    @SerialName("job_title")
    val jobTitle: String? = null,
    val company: String? = null,
    @SerialName("photo_url")
    val photoUrl: String,
    val website: String? = null,
    val twitter: String? = null,
    val mastodon: String? = null,
    val github: String? = null,
    val linkedin: String? = null
) : Validator {
    override fun validate(): List<String> {
        val errors = arrayListOf<String>()
        if (pronouns?.contains("/") == false) errors.add("Pronounce should contain separator '/'")
        if (twitter?.contains("twitter.com") == false) errors.add("Your twitter url is malformed")
        if (github?.contains("github.com") == false) errors.add("Your github url is malformed")
        if (linkedin?.contains("linkedin.com") == false) errors.add("Your linkedin url is malformed")
        return errors
    }
}
