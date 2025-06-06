package com.paligot.confily.core.di

import com.paligot.confily.core.events.EventInteractor
import com.paligot.confily.core.events.EventRepository
import com.paligot.confily.core.maps.MapInteractor
import com.paligot.confily.core.networking.UserInteractor
import com.paligot.confily.core.networking.UserRepository
import com.paligot.confily.core.partners.PartnerInteractor
import com.paligot.confily.core.partners.PartnerRepository
import com.paligot.confily.core.schedules.SessionRepository
import com.paligot.confily.core.sessions.SessionInteractor
import com.paligot.confily.core.speakers.SpeakerInteractor
import com.paligot.confily.core.speakers.SpeakerRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module
import platform.Foundation.NSBundle

val buildConfigModule = module {
    single(named(IsDebugNamed)) { false }
    single(named(ApplicationIdNamed)) { "com.paligot.confily.ios" }
    single(named(ConfilyBaseUrlNamed)) {
        NSBundle.mainBundle.objectForInfoDictionaryKey(key = "BASE_URL_C4H") as String
    }
    single(named(EventDefaultLanguage)) {
        NSBundle.mainBundle.objectForInfoDictionaryKey(key = "DEFAULT_LANGUAGE") as String
    }
}

class RepositoryHelper : KoinComponent {
    val sessionRepository: SessionRepository by inject()
    val partnerRepository: PartnerRepository by inject()
    val eventRepository: EventRepository by inject()
    val speakerRepository: SpeakerRepository by inject()
    val userRepository: UserRepository by inject()
}

class InteractorHelper : KoinComponent {
    val eventInteractor: EventInteractor by inject()
    val sessionInteractor: SessionInteractor by inject()
    val speakerInteractor: SpeakerInteractor by inject()
    val partnerInteractor: PartnerInteractor by inject()
    val userInteractor: UserInteractor by inject()
    val mapInteractor: MapInteractor by inject()
}

val interactorsModule = module {
    single { EventInteractor(get()) }
    single { SessionInteractor(get(), get()) }
    single { SpeakerInteractor(get(), get()) }
    single { PartnerInteractor(get()) }
    single { UserInteractor(get()) }
    single { MapInteractor(get()) }
}

fun initKoin() {
    startKoin {
        modules(buildConfigModule, repositoriesModule, interactorsModule)
    }
}
