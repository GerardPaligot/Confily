package org.gdglille.devfest.android.theme.m3.speakers.sample.fakes

import com.paligot.confily.models.AgendaV4
import com.paligot.confily.models.Category
import com.paligot.confily.models.Format
import com.paligot.confily.models.ScheduleItemV4
import com.paligot.confily.models.Session
import com.paligot.confily.models.Speaker
import kotlinx.datetime.Clock
import org.gdglille.devfest.android.core.models.factory.builder
import kotlin.time.Duration

object AgendaFake {
    private val startInstant = Clock.System.now().plus(Duration.parse("1d"))

    val schedule = ScheduleItemV4.builder()
        .id("session-id")
        .startTime(startInstant)
        .endTime(startInstant.plus(Duration.parse("50m")))
        .room("Room 1")
        .build()
    val format = Format.builder().id("format-id").name("Conference").time(50).build()
    val category =
        Category.builder().id("category-id").name("Mobile").color("blue").icon("mobile").build()
    val speakerGe =
        Speaker.builder().id("speaker-id-gerard").displayName("Gerard").bio("My bio").build()
    val speakerAu =
        Speaker.builder().id("speaker-id-aurore").displayName("Aurore").bio("My bio").build()
    val session = Session.Talk.builder()
        .id(schedule.id)
        .title("Title")
        .abstract("Abstract")
        .categoryId(category.id)
        .formatId(format.id)
        .language("English")
        .speakers(listOf(speakerGe.id))
        .build()
    val agenda = AgendaV4(
        schedules = listOf(schedule),
        sessions = listOf(session),
        formats = listOf(format),
        categories = listOf(category),
        speakers = listOf(speakerGe, speakerAu)
    )
}
