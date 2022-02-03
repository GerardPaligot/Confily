package com.paligot.conferences.backend.speakers

import com.paligot.conferences.models.Speaker
import com.paligot.conferences.models.inputs.SpeakerInput

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
