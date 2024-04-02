package org.gdglille.devfest.database.mappers

import kotlinx.collections.immutable.ImmutableList
import org.gdglille.devfest.android.shared.resources.Resource
import org.gdglille.devfest.android.shared.resources.text_speaker_activity
import org.gdglille.devfest.db.SelectSpeakersByTalkId
import org.gdglille.devfest.models.Speaker
import org.gdglille.devfest.models.ui.SpeakerItemUi
import org.gdglille.devfest.models.ui.SpeakerUi
import org.gdglille.devfest.models.ui.TalkItemUi
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.getString
import org.gdglille.devfest.db.Speaker as SpeakerDb

suspend fun SpeakerDb.convertToSpeakerUi(
    talks: ImmutableList<TalkItemUi>
): SpeakerUi = SpeakerUi(
    name = display_name,
    pronouns = pronouns,
    bio = bio,
    jobTitle = job_title,
    company = company,
    activity = displayActivity(),
    url = photo_url,
    twitterUrl = twitter,
    mastodonUrl = mastodon,
    githubUrl = github,
    linkedinUrl = linkedin,
    websiteUrl = website,
    talks = talks
)

suspend fun SpeakerDb.convertToSpeakerItemUi(): SpeakerItemUi = SpeakerItemUi(
    id = id,
    name = display_name,
    pronouns = pronouns,
    company = displayActivity() ?: "",
    url = photo_url
)

suspend fun SelectSpeakersByTalkId.convertSpeakerItemUi() = SpeakerItemUi(
    id = id,
    name = display_name,
    pronouns = pronouns,
    company = displayActivity() ?: "",
    url = photo_url
)

@OptIn(ExperimentalResourceApi::class)
suspend fun SpeakerDb.displayActivity() = when {
    job_title != null && company != null -> getString(
        Resource.string.text_speaker_activity,
        job_title,
        company
    )

    job_title == null && company != null -> company
    job_title != null && company == null -> job_title
    else -> null
}

@OptIn(ExperimentalResourceApi::class)
suspend fun SelectSpeakersByTalkId.displayActivity() = when {
    job_title != null && company != null -> getString(
        Resource.string.text_speaker_activity,
        job_title,
        company
    )

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
