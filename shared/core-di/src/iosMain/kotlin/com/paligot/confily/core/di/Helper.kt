package com.paligot.confily.core.di

import com.paligot.confily.core.repositories.AgendaRepository
import com.paligot.confily.core.repositories.EventRepository
import com.paligot.confily.core.repositories.UserRepository
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
    val eventRepository: EventRepository by inject()
    val speakerRepository: SpeakerRepository by inject()
    val userRepository: UserRepository by inject()
}

fun initKoin() {
    startKoin {
        modules(buildConfigModule, repositoriesModule)
    }
}
