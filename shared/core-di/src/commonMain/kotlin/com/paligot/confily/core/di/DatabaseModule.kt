package com.paligot.confily.core.di

import cafe.adriel.lyricist.LanguageTag
import cafe.adriel.lyricist.Lyricist
import com.paligot.confily.core.database.AgendaDao
import com.paligot.confily.core.database.EventDao
import com.paligot.confily.core.database.FeaturesActivatedDao
import com.paligot.confily.core.database.PartnerDao
import com.paligot.confily.core.database.UserDao
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
    single<CoroutineContext> { Dispatchers.IO }
    single { AgendaDao(db = get(), settings = get()) }
    single { EventDao(db = get(), settings = get(), dispatcher = get()) }
    single { FeaturesActivatedDao(db = get(), settings = get(), dispatcher = get()) }
    single { PartnerDao(db = get(), platform = get(), dispatcher = get()) }
    single { ScheduleDao(db = get(), settings = get(), lyricist = get(), dispatcher = get()) }
    single { SpeakerDao(db = get(), lyricist = get(), dispatcher = get()) }
    single { UserDao(db = get(), platform = get(), dispatcher = get()) }
}
