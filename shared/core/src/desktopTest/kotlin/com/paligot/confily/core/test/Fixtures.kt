package com.paligot.confily.core.test

import com.paligot.confily.models.AgendaV4
import com.paligot.confily.models.Category
import com.paligot.confily.models.EventMap
import com.paligot.confily.models.Format
import com.paligot.confily.models.MapPictogram
import com.paligot.confily.models.MapShape
import com.paligot.confily.models.MappingType
import com.paligot.confily.models.Offset
import com.paligot.confily.models.PictogramType
import com.paligot.confily.models.ScheduleItemV4
import com.paligot.confily.models.Session
import com.paligot.confily.models.Speaker
import com.paligot.confily.models.Tag

object Fixtures {
    fun category(id: String, name: String = id) =
        Category(id = id, name = name, color = "#FFFFFF", icon = "icon")

    fun format(id: String, name: String = id) =
        Format(id = id, name = name, time = 30)

    fun tag(id: String, name: String = id) =
        Tag(id = id, name = name)

    fun speaker(id: String) = Speaker(
        id = id,
        displayName = "Speaker $id",
        pronouns = null,
        bio = "bio",
        jobTitle = null,
        company = null,
        photoUrl = "https://photo/$id",
        socials = emptyList()
    )

    fun talk(
        id: String,
        categoryId: String,
        formatId: String,
        tagIds: List<String> = emptyList(),
        speakers: List<String> = emptyList()
    ) = Session.Talk(
        id = id,
        title = "Talk $id",
        level = null,
        abstract = "abstract",
        categoryId = categoryId,
        tagIds = tagIds,
        formatId = formatId,
        language = "en",
        speakers = speakers,
        linkSlides = null,
        linkReplay = null,
        openFeedback = null
    )

    fun schedule(id: String, sessionId: String) = ScheduleItemV4(
        id = id,
        order = 0,
        date = "2026-06-11",
        startTime = "2026-06-11T09:00:00",
        endTime = "2026-06-11T09:30:00",
        room = "Room A",
        sessionId = sessionId
    )

    fun agenda(
        sessions: List<Session> = emptyList(),
        schedules: List<ScheduleItemV4> = emptyList(),
        categories: List<Category> = emptyList(),
        formats: List<Format> = emptyList(),
        tags: List<Tag> = emptyList(),
        speakers: List<Speaker> = emptyList()
    ) = AgendaV4(
        schedules = schedules,
        sessions = sessions,
        formats = formats,
        tags = tags,
        categories = categories,
        speakers = speakers
    )

    fun shape(order: Int = 0) = MapShape(
        order = order,
        name = "shape-$order",
        description = null,
        start = Offset(0f, 0f),
        end = Offset(1f, 1f),
        type = MappingType.Room
    )

    fun pictogram(order: Int = 0) = MapPictogram(
        order = order,
        name = "picto-$order",
        description = null,
        position = Offset(0f, 0f),
        type = PictogramType.Coffee
    )

    fun map(
        id: String,
        shapes: List<MapShape> = emptyList(),
        pictograms: List<MapPictogram> = emptyList()
    ) = EventMap(
        id = id,
        name = "Map $id",
        color = "#000000",
        colorSelected = "#111111",
        order = 0,
        url = "https://map/$id",
        filledUrl = null,
        pictoSize = 24,
        shapes = shapes,
        pictograms = pictograms
    )
}
