package com.paligot.conferences.android.screens

import com.paligot.conferences.android.components.speakers.SpeakerItemUi
import com.paligot.conferences.android.components.speakers.SpeakerUi
import com.paligot.conferences.android.components.talks.TalkItemUi
import com.paligot.conferences.android.components.talks.TalkUi
import com.paligot.conferences.android.screens.agenda.AgendaUi
import com.paligot.conferences.models.Agenda
import com.paligot.conferences.models.ScheduleItem
import com.paligot.conferences.models.Speaker
import com.paligot.conferences.models.Talk

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