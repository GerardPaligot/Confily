package org.gdglille.devfest.backend.speakers

import org.gdglille.devfest.models.Speaker
import org.gdglille.devfest.models.inputs.SpeakerInput

fun org.gdglille.devfest.backend.network.Speaker.convertToDb(url: String): SpeakerDb = SpeakerDb(
  id = this.uid,
  displayName = this.displayName,
  bio = this.bio ?: "",
  company = this.company,
  photoUrl = url,
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
