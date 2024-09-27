package com.paligot.confily.core.di

import com.paligot.confily.core.networking.NetworkingRepository
import com.paligot.confily.core.partners.PartnerRepository
import com.paligot.confily.core.repositories.AgendaRepository
import com.paligot.confily.core.repositories.EventRepository
import com.paligot.confily.core.schedules.SchedulesRepository
import com.paligot.confily.core.speakers.SpeakerRepository
import org.koin.dsl.module

val repositoriesModule = module {
    includes(databasesModule, networksModule)
    single {
        AgendaRepository.Factory.create(
            api = get(),
            agendaDao = get(),
            eventDao = get(),
            featuresDao = get(),
            qrCodeGenerator = get()
        )
    }
    single { SchedulesRepository.Factory.create(get(), get()) }
    single { EventRepository.Factory.create(api = get(), eventDao = get()) }
    single { SpeakerRepository.Factory.create(get(), eventDao = get()) }
    single { PartnerRepository.Factory.create(eventDao = get(), partnerDao = get()) }
    single {
        NetworkingRepository.Factory.create(
            userDao = get(),
            eventDao = get(),
            qrCodeGenerator = get()
        )
    }
}
