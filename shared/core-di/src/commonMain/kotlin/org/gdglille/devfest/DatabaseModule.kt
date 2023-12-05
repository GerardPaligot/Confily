package org.gdglille.devfest

import com.russhwolf.settings.ExperimentalSettingsApi
import org.gdglille.devfest.database.EventDao
import org.gdglille.devfest.database.FeaturesActivatedDao
import org.gdglille.devfest.database.PartnerDao
import org.gdglille.devfest.database.ScheduleDao
import org.gdglille.devfest.database.SpeakerDao
import org.gdglille.devfest.database.TalkDao
import org.gdglille.devfest.database.UserDao
import org.koin.dsl.module

@OptIn(ExperimentalSettingsApi::class)
val databasesModule = module {
    includes(platformModule)
    single { EventDao(db = get(), settings = get()) }
    single { FeaturesActivatedDao(db = get(), settings = get()) }
    single { PartnerDao(db = get(), platform = get()) }
    single { ScheduleDao(db = get(), settings = get(), platform = get()) }
    single { SpeakerDao(db = get(), platform = get()) }
    single { TalkDao(db = get(), platform = get()) }
    single { UserDao(db = get(), platform = get()) }
}
