package com.paligot.confily.core.speakers

import com.paligot.confily.db.Speaker
import com.paligot.confily.models.ui.SpeakerItemUi
import com.paligot.confily.models.ui.SpeakerUi
import com.paligot.confily.models.ui.TalkItemUi
import com.paligot.confily.resources.Strings
import kotlinx.collections.immutable.ImmutableList

fun Speaker.convertToSpeakerUi(
    talks: ImmutableList<TalkItemUi>,
    strings: Strings
): SpeakerUi = SpeakerUi(
    name = display_name,
    pronouns = pronouns,
    bio = bio,
    jobTitle = job_title,
    company = company,
    activity = displayActivity(strings),
    url = photo_url,
    twitterUrl = twitter,
    mastodonUrl = mastodon,
    githubUrl = github,
    linkedinUrl = linkedin,
    websiteUrl = website,
    talks = talks
)

fun Speaker.convertToSpeakerItemUi(strings: Strings): SpeakerItemUi = SpeakerItemUi(
    id = id,
    name = display_name,
    pronouns = pronouns,
    company = displayActivity(strings) ?: "",
    url = photo_url
)

private fun Speaker.displayActivity(strings: Strings) = when {
    job_title != null && company != null -> strings.texts.speakerActivity(job_title!!, company!!)
    job_title == null && company != null -> company
    job_title != null && company == null -> job_title
    else -> null
}
