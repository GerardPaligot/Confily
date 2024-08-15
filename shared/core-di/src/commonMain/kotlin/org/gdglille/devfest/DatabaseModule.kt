package org.gdglille.devfest

import cafe.adriel.lyricist.LanguageTag
import cafe.adriel.lyricist.Lyricist
import com.russhwolf.settings.ExperimentalSettingsApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.gdglille.devfest.android.shared.resources.EnStrings
import org.gdglille.devfest.android.shared.resources.FrStrings
import org.gdglille.devfest.database.EventDao
import org.gdglille.devfest.database.FeaturesActivatedDao
import org.gdglille.devfest.database.PartnerDao
import org.gdglille.devfest.database.ScheduleDao
import org.gdglille.devfest.database.SpeakerDao
import org.gdglille.devfest.database.TalkDao
import org.gdglille.devfest.database.UserDao
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
    single { EventDao(db = get(), settings = get(), dispatcher = get()) }
    single { FeaturesActivatedDao(db = get(), settings = get(), dispatcher = get()) }
    single { PartnerDao(db = get(), platform = get(), dispatcher = get()) }
    single { ScheduleDao(db = get(), settings = get(), lyricist = get(), dispatcher = get()) }
    single { SpeakerDao(db = get(), lyricist = get(), dispatcher = get()) }
    single { TalkDao(db = get(), lyricist = get(), dispatcher = get()) }
    single { UserDao(db = get(), platform = get(), dispatcher = get()) }
}
