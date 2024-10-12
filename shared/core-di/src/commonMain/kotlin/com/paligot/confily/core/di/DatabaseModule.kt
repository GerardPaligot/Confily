package com.paligot.confily.core.di

import cafe.adriel.lyricist.LanguageTag
import cafe.adriel.lyricist.Lyricist
import com.paligot.confily.core.Platform
import com.paligot.confily.core.agenda.AgendaDao
import com.paligot.confily.core.agenda.FeaturesActivatedDao
import com.paligot.confily.core.kvalue.ConferenceSettings
import com.paligot.confily.core.events.EventDao
import com.paligot.confily.core.networking.UserDao
import com.paligot.confily.core.partners.PartnerDao
import com.paligot.confily.core.schedules.ScheduleDao
import com.paligot.confily.core.speakers.SpeakerDao
import com.paligot.confily.resources.EnStrings
import com.paligot.confily.resources.FrStrings
import com.russhwolf.settings.ExperimentalSettingsApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.qualifier.named
import org.koin.dsl.module
import kotlin.coroutines.CoroutineContext

@OptIn(ExperimentalSettingsApi::class)
val databasesModule = module {
    includes(platformModule)
    single {
        Lyricist(
            defaultLanguageTag = "en",
            translations = mapOf("en" to EnStrings, "fr" to FrStrings)
        ).apply {
            this.languageTag = get<LanguageTag>(named(AcceptLanguageNamed)).split("-").first()
        }
    }
    single { ConferenceSettings(get()) }
    single<CoroutineContext> { Dispatchers.IO }
    single { AgendaDao(db = get(), hasSvgSupport = get<Platform>().hasSupportSVG) }
    single { EventDao(db = get(), dispatcher = get()) }
    single { FeaturesActivatedDao(db = get(), dispatcher = get()) }
    single { PartnerDao(db = get(), dispatcher = get()) }
    single { ScheduleDao(db = get(), settings = get(), lyricist = get(), dispatcher = get()) }
    single { SpeakerDao(db = get(), lyricist = get(), dispatcher = get()) }
    single { UserDao(db = get(), dispatcher = get()) }
}
