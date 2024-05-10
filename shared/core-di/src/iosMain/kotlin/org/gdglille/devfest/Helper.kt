package org.gdglille.devfest

import org.gdglille.devfest.repositories.AgendaRepository
import org.gdglille.devfest.repositories.EventRepository
import org.gdglille.devfest.repositories.SpeakerRepository
import org.gdglille.devfest.repositories.UserRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module
import platform.Foundation.NSBundle

val buildConfigModule = module {
    single(named(IsDebugNamed)) { false }
    single(named(ApplicationIdNamed)) { "org.gdglille.devfest.ios" }
    single(named(Conference4HallBaseUrlNamed)) {
        NSBundle.mainBundle.objectForInfoDictionaryKey(key = "BASE_URL_C4H") as String
    }
}

class RepositoryHelper : KoinComponent {
    val agendaRepository: AgendaRepository by inject()
    val eventRepository: EventRepository by inject()
    val speakerRepository: SpeakerRepository by inject()
    val userRepository: UserRepository by inject()
}

fun initKoin() {
    startKoin {
        modules(buildConfigModule, repositoriesModule)
    }
}
