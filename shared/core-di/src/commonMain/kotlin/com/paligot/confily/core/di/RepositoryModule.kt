package com.paligot.confily.core.di

import com.paligot.confily.core.repositories.AgendaRepository
import com.paligot.confily.core.repositories.EventRepository
import com.paligot.confily.core.repositories.SpeakerRepository
import com.paligot.confily.core.repositories.UserRepository
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
