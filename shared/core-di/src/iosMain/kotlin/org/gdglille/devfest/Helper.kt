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

val buildConfigModule = module {
    single(named(IsDebugNamed)) { true }
    single(named(ApplicationIdNamed)) { "org.gdglille.devfest.ios" }
    single(named(Conference4HallBaseUrlNamed)) { "https://cms4partners-ce427.nw.r.appspot.com" }
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
