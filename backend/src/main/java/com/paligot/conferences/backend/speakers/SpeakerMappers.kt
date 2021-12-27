package com.paligot.conferences.backend.speakers

import com.paligot.conferences.backend.schedulers.ScheduleDb
import com.paligot.conferences.backend.schedulers.ScheduleInput
import com.paligot.conferences.models.Speaker

fun com.paligot.conferences.backend.network.Speaker.convertToDb(): SpeakerDb = SpeakerDb(
  id = this.uid,
  displayName = this.displayName,
  bio = this.bio ?: "",
  company = this.company,
  photoUrl = this.photoURL,
  twitter = null,
  github = null
)

fun SpeakerDb.convertToModel(): Speaker = Speaker(
  id = this.id,
  displayName = this.displayName,
  bio = this.bio,
  company = this.company,
  photoUrl = this.photoUrl,
  twitter = this.twitter,
  github = this.github
)

fun SpeakerInput.convertToDb(id: String? = null) = SpeakerDb(
  id = id ?: "",
  displayName = this.displayName,
  bio = this.bio,
  company = this.company,
  photoUrl = this.photoUrl,
  twitter = this.twitter,
  github = this.github
)
