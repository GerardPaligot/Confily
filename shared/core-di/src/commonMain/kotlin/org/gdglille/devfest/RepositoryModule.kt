package org.gdglille.devfest

import org.gdglille.devfest.repositories.AgendaRepository
import org.gdglille.devfest.repositories.EventRepository
import org.gdglille.devfest.repositories.SpeakerRepository
import org.gdglille.devfest.repositories.UserRepository
import org.koin.dsl.module

val repositoriesModule = module {
    includes(databasesModule, networksModule)
    single {
        AgendaRepository.Factory.create(
            api = get(),
            scheduleDao = get(),
            speakerDao = get(),
            talkDao = get(),
            eventDao = get(),
            partnerDao = get(),
            featuresDao = get(),
            qrCodeGenerator = get()
        )
    }
    single { EventRepository.Factory.create(api = get(), eventDao = get()) }
    single { SpeakerRepository.Factory.create(get(), eventDao = get()) }
    single {
        UserRepository.Factory.create(userDao = get(), eventDao = get(), qrCodeGenerator = get())
    }
}
