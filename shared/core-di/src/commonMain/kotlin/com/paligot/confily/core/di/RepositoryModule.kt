package com.paligot.confily.core.di

import com.paligot.confily.core.events.EventRepository
import com.paligot.confily.core.networking.UserRepository
import com.paligot.confily.core.partners.PartnerRepository
import com.paligot.confily.core.schedules.SessionRepository
import com.paligot.confily.core.speakers.SpeakerRepository
import org.koin.dsl.module

val repositoriesModule = module {
    includes(databasesModule, networksModule, fileSystemModule)
    single {
        SessionRepository.Factory.create(eventDao = get(), sessionDao = get(), settings = get())
    }
    single {
        EventRepository.Factory.create(
            api = get(),
            settings = get(),
            eventDao = get(),
            sessionDao = get(),
            userDao = get(),
            partnerDao = get(),
            socialDao = get(),
            qrCodeGenerator = get()
        )
    }
    single {
        SpeakerRepository.Factory.create(
            speakerDao = get(),
            sessionDao = get(),
            socialDao = get(),
            settings = get()
        )
    }
    single {
        PartnerRepository.Factory.create(
            settings = get(),
            eventDao = get(),
            partnerDao = get(),
            socialDao = get()
        )
    }
    single {
        UserRepository.Factory.create(
            userDao = get(),
            settings = get(),
            conferenceFs = get(),
            qrCodeGenerator = get()
        )
    }
}
