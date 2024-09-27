package com.paligot.confily.core.database.mappers

import com.paligot.confily.db.SelectSpeakersByTalkId
import com.paligot.confily.models.Speaker
import com.paligot.confily.models.ui.SpeakerItemUi
import com.paligot.confily.resources.Strings
import com.paligot.confily.db.Speaker as SpeakerDb

fun SelectSpeakersByTalkId.convertSpeakerItemUi(strings: Strings) = SpeakerItemUi(
    id = id,
    name = display_name,
    pronouns = pronouns,
    company = displayActivity(strings) ?: "",
    url = photo_url
)

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
