package com.paligot.confily.core.di

import com.paligot.confily.core.agenda.AgendaRepository
import com.paligot.confily.core.events.EventRepository
import com.paligot.confily.core.networking.NetworkingRepository
import com.paligot.confily.core.partners.PartnerRepository
import com.paligot.confily.core.schedules.SessionRepository
import com.paligot.confily.core.speakers.SpeakerRepository
import org.koin.dsl.module

val repositoriesModule = module {
    includes(databasesModule, networksModule, fileSystemModule)
    single {
        AgendaRepository.Factory.create(
            api = get(),
            agendaDao = get(),
            settings = get(),
            featuresDao = get()
        )
    }
    single { SessionRepository.Factory.create(sessionDao = get(), settings = get()) }
    single {
        EventRepository.Factory.create(
            api = get(),
            settings = get(),
            eventDao = get(),
            qrCodeGenerator = get()
        )
    }
    single {
        SpeakerRepository.Factory.create(speakerDao = get(), sessionDao = get(), settings = get())
    }
    single {
        PartnerRepository.Factory.create(settings = get(), eventDao = get(), partnerDao = get())
    }
    single {
        NetworkingRepository.Factory.create(
            userDao = get(),
            settings = get(),
            conferenceFs = get(),
            qrCodeGenerator = get()
        )
    }
}
