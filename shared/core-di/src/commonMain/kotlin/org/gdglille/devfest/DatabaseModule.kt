package org.gdglille.devfest

import com.russhwolf.settings.ExperimentalSettingsApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
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
    single { EventDao(db = get(), settings = get(), dispatcher = Dispatchers.IO) }
    single { FeaturesActivatedDao(db = get(), settings = get(), dispatcher = Dispatchers.IO) }
    single { PartnerDao(db = get(), platform = get(), dispatcher = Dispatchers.IO) }
    single { ScheduleDao(db = get(), settings = get(), dispatcher = Dispatchers.IO) }
    single { SpeakerDao(db = get(), dispatcher = Dispatchers.IO) }
    single { TalkDao(db = get(), dispatcher = Dispatchers.IO) }
    single { UserDao(db = get(), platform = get(), dispatcher = Dispatchers.IO) }
}
