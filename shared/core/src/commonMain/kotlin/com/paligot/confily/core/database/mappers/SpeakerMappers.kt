package com.paligot.confily.core.database.mappers

import com.paligot.confily.db.SelectSpeakersByTalkId
import com.paligot.confily.models.Speaker
import com.paligot.confily.models.ui.SpeakerItemUi
import com.paligot.confily.models.ui.SpeakerUi
import com.paligot.confily.models.ui.TalkItemUi
import com.paligot.confily.resources.Strings
import kotlinx.collections.immutable.ImmutableList
import com.paligot.confily.db.Speaker as SpeakerDb

fun SpeakerDb.convertToSpeakerUi(
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

fun SpeakerDb.convertToSpeakerItemUi(strings: Strings): SpeakerItemUi = SpeakerItemUi(
    id = id,
    name = display_name,
    pronouns = pronouns,
    company = displayActivity(strings) ?: "",
    url = photo_url
)

fun SelectSpeakersByTalkId.convertSpeakerItemUi(strings: Strings) = SpeakerItemUi(
    id = id,
    name = display_name,
    pronouns = pronouns,
    company = displayActivity(strings) ?: "",
    url = photo_url
)

fun SpeakerDb.displayActivity(strings: Strings) = when {
    job_title != null && company != null -> strings.texts.speakerActivity(job_title!!, company!!)
    job_title == null && company != null -> company
    job_title != null && company == null -> job_title
    else -> null
}

fun SelectSpeakersByTalkId.displayActivity(strings: Strings) = when {
    job_title != null && company != null -> strings.texts.speakerActivity(job_title!!, company!!)
    job_title == null && company != null -> company
    job_title != null && company == null -> job_title
    else -> null
}

fun Speaker.convertToDb(eventId: String): SpeakerDb = SpeakerDb(
    id = id,
    display_name = displayName,
    pronouns = pronouns,
    bio = bio,
    job_title = jobTitle,
    company = company,
    photo_url = photoUrl,
    twitter = twitter,
    mastodon = mastodon,
    github = github,
    linkedin = linkedin,
    website = website,
    event_id = eventId
)
