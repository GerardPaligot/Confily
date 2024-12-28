package com.paligot.confily.core.di

import cafe.adriel.lyricist.LanguageTag
import cafe.adriel.lyricist.Lyricist
import com.paligot.confily.core.events.EventDao
import com.paligot.confily.core.events.EventDaoSettings
import com.paligot.confily.core.events.EventQueries
import com.paligot.confily.core.events.FeaturesActivatedQueries
import com.paligot.confily.core.events.MenuQueries
import com.paligot.confily.core.events.QAndAQueries
import com.paligot.confily.core.events.TeamMembersQueries
import com.paligot.confily.core.kvalue.ConferenceSettings
import com.paligot.confily.core.networking.UserDao
import com.paligot.confily.core.networking.UserDaoSettings
import com.paligot.confily.core.partners.PartnerDao
import com.paligot.confily.core.partners.PartnerDaoSettings
import com.paligot.confily.core.partners.PartnerQueries
import com.paligot.confily.core.schedules.CategoryQueries
import com.paligot.confily.core.schedules.FormatQueries
import com.paligot.confily.core.schedules.SessionDao
import com.paligot.confily.core.schedules.SessionDaoSettings
import com.paligot.confily.core.schedules.SessionQueries
import com.paligot.confily.core.socials.SocialDao
import com.paligot.confily.core.socials.SocialDaoSettings
import com.paligot.confily.core.socials.SocialQueries
import com.paligot.confily.core.speakers.SpeakerDao
import com.paligot.confily.core.speakers.SpeakerDaoSettings
import com.paligot.confily.core.speakers.SpeakerQueries
import com.paligot.confily.resources.EnStrings
import com.paligot.confily.resources.FrStrings
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import kotlin.coroutines.CoroutineContext

actual val databasesModule: Module = module {
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
    single<CoroutineContext> { Dispatchers.Default }
    single { EventQueries(settings = get()) }
    single { SocialQueries(settings = get()) }
    single { FeaturesActivatedQueries(settings = get()) }
    single { MenuQueries(settings = get()) }
    single { QAndAQueries(settings = get()) }
    single { PartnerQueries(settings = get()) }
    single { CategoryQueries(settings = get()) }
    single { FormatQueries(settings = get()) }
    single { SpeakerQueries(settings = get()) }
    single { TeamMembersQueries(settings = get()) }
    single {
        SessionQueries(
            settings = get(),
            categoryQueries = get(),
            formatQueries = get(),
            speakerQueries = get()
        )
    }
    single<EventDao> {
        EventDaoSettings(
            eventQueries = get(),
            qAndAQueries = get(),
            menuQueries = get(),
            teamMembersQueries = get(),
            socialQueries = get(),
            featuresActivatedQueries = get()
        )
    }
    single<SocialDao> { SocialDaoSettings(socialQueries = get()) }
    single<PartnerDao> {
        PartnerDaoSettings(
            partnerQueries = get(),
            socialQueries = get(),
            hasSvgSupport = true
        )
    }
    single<SessionDao> {
        SessionDaoSettings(
            settings = get(),
            sessionQueries = get(),
            categoryQueries = get(),
            formatQueries = get(),
            speakerQueries = get(),
            socialQueries = get()
        )
    }
    single<SpeakerDao> {
        SpeakerDaoSettings(speakerQueries = get())
    }
    single<UserDao> { UserDaoSettings() }
}
