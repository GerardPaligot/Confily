package com.paligot.conferences.backend.talks

import com.paligot.conferences.backend.network.Category
import com.paligot.conferences.backend.network.Format
import com.paligot.conferences.backend.speakers.SpeakerDb
import com.paligot.conferences.backend.speakers.convertToModel
import com.paligot.conferences.models.Talk

fun com.paligot.conferences.backend.network.Talk.convertToDb(
  categories: List<Category>,
  formats: List<Format>
): TalkDb = TalkDb(
  id = this.id,
  title = this.title,
  level = this.level,
  abstract = this.abstract,
  category = categories.find { it.id == this.categories }?.name ?: error("Category not found for ${this.id} talk"),
  format = formats.find { it.id == this.formats }?.name ?: error("Format not found for ${this.id} talk"),
  speakerIds = this.speakers,
  openFeedback = null
)

fun TalkDb.convertToModel(speakers: List<SpeakerDb>): Talk = Talk(
  id = this.id,
  title = this.title,
  level = this.level,
  abstract = this.abstract,
  category = this.category,
  format = this.format,
  speakers = speakers.filter { this.speakerIds.contains(it.id) }.map { it.convertToModel() },
  openFeedback = this.openFeedback
)

fun TalkInput.convertToDb(id: String? = null): TalkDb = TalkDb(
  id = id ?: "",
  title = this.title,
  level = this.level,
  abstract = this.abstract,
  category = this.category,
  format = this.format,
  speakerIds = this.speakerIds,
  openFeedback = this.openFeedback
)
