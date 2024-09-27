package com.paligot.confily.core.di

import com.paligot.confily.core.agenda.AgendaRepository
import com.paligot.confily.core.events.EventRepository
import com.paligot.confily.core.networking.NetworkingRepository
import com.paligot.confily.core.partners.PartnerRepository
import com.paligot.confily.core.schedules.SchedulesRepository
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
}

class RepositoryHelper : KoinComponent {
    val agendaRepository: AgendaRepository by inject()
    val schedulesRepository: SchedulesRepository by inject()
    val partnerRepository: PartnerRepository by inject()
    val eventRepository: EventRepository by inject()
    val speakerRepository: SpeakerRepository by inject()
    val networkingRepository: NetworkingRepository by inject()
}

fun initKoin() {
    startKoin {
        modules(buildConfigModule, repositoriesModule)
    }
}
