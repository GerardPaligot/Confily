package org.gdglille.devfest.android.theme.m3.schedules.sample

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ActivityScenario
import org.gdglille.devfest.android.core.sample.BuildConfig
import org.gdglille.devfest.android.core.test.KoinTestRule
import org.gdglille.devfest.android.core.test.instrumentedModule
import org.gdglille.devfest.android.theme.m3.schedules.di.scheduleModule
import org.gdglille.devfest.android.theme.m3.schedules.test.ScheduleGridPOM
import org.gdglille.devfest.database.EventDao
import org.gdglille.devfest.database.ScheduleDao
import org.gdglille.devfest.models.AgendaV4
import org.gdglille.devfest.models.Category
import org.gdglille.devfest.models.Format
import org.gdglille.devfest.models.ScheduleItemV4
import org.gdglille.devfest.models.Session
import org.gdglille.devfest.models.Speaker
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

class MainActivityTest : KoinTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val koinTestRule = KoinTestRule(
        modules = listOf(scheduleModule, instrumentedModule)
    )

    private val eventDao by inject<EventDao>()
    private val scheduleDao by inject<ScheduleDao>()

    @Before
    fun setup() {
        eventDao.insertEventId(BuildConfig.DEFAULT_EVENT)
    }

    @Test
    fun test() {
        val agenda = AgendaV4(
            schedules = listOf(ScheduleItemV4("session-id", 0, "2024-06-07", "2024-06-07T17:20:00.000", "2024-06-07T18:10:00.000", "Room 1", "session-id")),
            sessions = listOf(Session.Talk("session-id", "Title", null, "Abstract", "category-id", "format-id", "English", listOf("speaker-id"), null, null, null)),
            formats = listOf(Format("format-id", "Format", 50)),
            categories = listOf(Category("category-id", "Category", "blue", "mobile")),
            speakers = listOf(Speaker("speaker-id", "Gerard", null, "My bio", null, null, "", null, null, null, null, null))
        )
        scheduleDao.saveAgenda(BuildConfig.DEFAULT_EVENT, agenda)

        ActivityScenario.launch(MainActivity::class.java).use {
            val scheduleGridPOM = ScheduleGridPOM(composeTestRule)
            val filtersPOM = scheduleGridPOM.clickOnFiltersIcon()
        }
    }
}
