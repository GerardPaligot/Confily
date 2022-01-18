package com.paligot.conferences.android.screens

import com.paligot.conferences.android.R
import com.paligot.conferences.android.components.events.EventInfoUi
import com.paligot.conferences.android.components.events.PartnerItemUi
import com.paligot.conferences.android.components.speakers.SpeakerItemUi
import com.paligot.conferences.android.components.speakers.SpeakerUi
import com.paligot.conferences.android.components.talks.TalkItemUi
import com.paligot.conferences.android.components.talks.TalkUi
import com.paligot.conferences.android.screens.agenda.AgendaUi
import com.paligot.conferences.android.screens.event.EventUi
import com.paligot.conferences.android.screens.event.PartnerGroupsUi
import com.paligot.conferences.models.Agenda
import com.paligot.conferences.models.Event
import com.paligot.conferences.models.Partner
import com.paligot.conferences.models.ScheduleItem
import com.paligot.conferences.models.Speaker
import java.text.SimpleDateFormat
import java.util.*

fun Speaker.convertToModelUi(): SpeakerUi = SpeakerUi(
    url = this.photoUrl,
    name = this.displayName,
    company = this.company ?: "",
    bio = this.bio,
    twitter = this.twitter?.split("twitter.com/")?.get(1),
    twitterUrl = this.twitter,
    github = this.github?.split("github.com/")?.get(1),
    githubUrl = this.github
)

fun Speaker.convertToModelItemUi(): SpeakerItemUi = SpeakerItemUi(
    id = this.id,
    name = this.displayName,
    company = this.company ?: "",
    url = this.photoUrl
)

fun ScheduleItem.convertToModelUi(): TalkUi = TalkUi(
    title = this.talk!!.title,
    date = this.time,
    room = this.room!!,
    level = this.talk!!.level,
    abstract = this.talk!!.abstract,
    speakers = this.talk!!.speakers.map { it.convertToModelItemUi() }
)

fun ScheduleItem.convertToModelItemUi(): TalkItemUi = TalkItemUi(
    id = this.id,
    room = this.room ?: "",
    title = this.talk?.title ?: "Pause",
    speakers = this.talk?.speakers?.map { it.displayName } ?: emptyList()
)

fun Agenda.convertToModelUi(): AgendaUi = AgendaUi(
    talks = this.talks.mapValues { talk -> talk.value.map { it.convertToModelItemUi() } }
)

fun Partner.convertToModelUi(): PartnerItemUi = PartnerItemUi(
    logoUrl = this.logoUrl,
    siteUrl = this.siteUrl!!,
    name = this.name
)

fun Event.convertToModelUi(): EventUi = EventUi(
    eventInfo = EventInfoUi(
        logo = R.drawable.ic_launcher_foreground,
        name = this.name,
        address = this.address.address,
        date = getDate(this.startDate, this.endDate),
        twitter = this.twitterUrl?.split("twitter.com/")?.get(1),
        twitterUrl = this.twitterUrl,
        linkedin = this.name,
        linkedinUrl = this.linkedinUrl,
        faqLink = this.faqLink!!,
        codeOfConductLink = this.codeOfConductLink!!
    ),
    partners = PartnerGroupsUi(
        golds = this.partners.golds.map { it.convertToModelUi() },
        silvers = this.partners.silvers.map { it.convertToModelUi() },
        bronzes = this.partners.bronzes.map { it.convertToModelUi() },
        others = this.partners.others.map { it.convertToModelUi() }
    )
)

fun getDate(from: String, to: String): String {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.FRANCE)
    val output = SimpleDateFormat("dd MMMM yyyy", Locale.FRANCE)
    if (from == to) {
        val date = format.parse(from)!!
        return output.format(date)
    }
    return "From ${output.format(format.parse(from)!!)} to ${output.format(format.parse(to)!!)}"
}
