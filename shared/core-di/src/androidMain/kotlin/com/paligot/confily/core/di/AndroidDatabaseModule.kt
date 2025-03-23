package com.paligot.confily.core.di

import cafe.adriel.lyricist.LanguageTag
import cafe.adriel.lyricist.Lyricist
import com.paligot.confily.core.Platform
import com.paligot.confily.core.events.EventDao
import com.paligot.confily.core.events.EventDaoSQLDelight
import com.paligot.confily.core.kvalue.ConferenceSettings
import com.paligot.confily.core.maps.MapDao
import com.paligot.confily.core.maps.MapDaoSQLDelight
import com.paligot.confily.core.networking.UserDao
import com.paligot.confily.core.networking.UserDaoSQLDelight
import com.paligot.confily.core.partners.PartnerDao
import com.paligot.confily.core.partners.PartnerDaoSQLDelight
import com.paligot.confily.core.schedules.SessionDao
import com.paligot.confily.core.schedules.SessionDaoSQLDelight
import com.paligot.confily.core.socials.SocialDao
import com.paligot.confily.core.socials.SocialDaoSQLDelight
import com.paligot.confily.core.speakers.SpeakerDao
import com.paligot.confily.core.speakers.SpeakerDaoSQLDelight
import com.paligot.confily.resources.EnStrings
import com.paligot.confily.resources.FrStrings
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module
import kotlin.coroutines.CoroutineContext

actual val databasesModule = module {
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
    single<SocialDao> { SocialDaoSQLDelight(db = get(), dispatcher = get()) }
    single<EventDao> {
        EventDaoSQLDelight(
            db = get(),
            language = get(named(AcceptLanguageNamed)),
            dispatcher = get()
        )
    }
    single<PartnerDao> {
        PartnerDaoSQLDelight(
            db = get(),
            hasSvgSupport = get<Platform>().hasSupportSVG,
            dispatcher = get()
        )
    }
    single<SessionDao> {
        SessionDaoSQLDelight(db = get(), settings = get(), dispatcher = get())
    }
    single<SpeakerDao> { SpeakerDaoSQLDelight(db = get(), dispatcher = get()) }
    single<UserDao> { UserDaoSQLDelight(db = get(), dispatcher = get()) }
    single<MapDao> { MapDaoSQLDelight(db = get(), dispatcher = get()) }
}
