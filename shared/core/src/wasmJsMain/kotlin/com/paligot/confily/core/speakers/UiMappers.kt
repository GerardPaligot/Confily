package com.paligot.confily.core.speakers

import com.paligot.confily.models.ui.SpeakerItemUi
import com.paligot.confily.models.ui.SpeakerUi
import com.paligot.confily.models.ui.TalkItemUi
import com.paligot.confily.resources.Strings
import kotlinx.collections.immutable.ImmutableList

fun SpeakerDb.convertToSpeakerUi(
    talks: ImmutableList<TalkItemUi>,
    strings: Strings
): SpeakerUi = SpeakerUi(
    name = displayName,
    pronouns = pronouns,
    bio = bio,
    jobTitle = jobTitle,
    company = company,
    activity = displayActivity(strings),
    url = photoUrl,
    twitterUrl = twitter,
    mastodonUrl = mastodon,
    githubUrl = github,
    linkedinUrl = linkedin,
    websiteUrl = website,
    talks = talks
)

fun SpeakerDb.convertSpeakerItemUi(strings: Strings) = SpeakerItemUi(
    id = id,
    name = displayName,
    pronouns = pronouns,
    company = displayActivity(strings) ?: "",
    url = photoUrl
)

fun SpeakerDb.displayActivity(strings: Strings) = when {
    jobTitle != null && company != null -> strings.texts.speakerActivity(jobTitle, company)
    jobTitle == null && company != null -> company
    jobTitle != null && company == null -> jobTitle
    else -> null
}
