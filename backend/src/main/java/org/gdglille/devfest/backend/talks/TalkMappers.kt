package org.gdglille.devfest.backend.talks

import org.gdglille.devfest.backend.events.EventDb
import org.gdglille.devfest.backend.events.openFeedbackUrl
import org.gdglille.devfest.backend.network.Category
import org.gdglille.devfest.backend.network.Format
import org.gdglille.devfest.backend.speakers.SpeakerDb
import org.gdglille.devfest.backend.speakers.convertToModel
import org.gdglille.devfest.models.Talk
import org.gdglille.devfest.models.inputs.TalkInput

fun org.gdglille.devfest.backend.network.Talk.convertToDb(
  categories: List<Category>,
  formats: List<Format>
): TalkDb = TalkDb(
  id = this.id,
  title = this.title,
  level = this.level,
  abstract = this.abstract,
  category = categories.find { it.id == this.categories }?.name ?: error("Category not found for ${this.id} talk"),
  format = formats.find { it.id == this.formats }?.name ?: error("Format not found for ${this.id} talk"),
  language = this.language,
  speakerIds = this.speakers,
)

fun TalkDb.convertToModel(speakers: List<SpeakerDb>, eventDb: EventDb): Talk = Talk(
  id = this.id,
  title = this.title,
  level = this.level,
  abstract = this.abstract,
  category = this.category,
  format = this.format,
  language = this.language,
  speakers = speakers.filter { this.speakerIds.contains(it.id) }.map { it.convertToModel() },
  openFeedback = eventDb.openFeedbackUrl()?.let { "$it/$id" } ?: run { null }
)

fun TalkInput.convertToDb(id: String? = null): TalkDb = TalkDb(
  id = id ?: "",
  title = this.title,
  level = this.level,
  abstract = this.abstract,
  category = this.category,
  format = this.format,
  language = this.language,
  speakerIds = this.speakerIds
)
