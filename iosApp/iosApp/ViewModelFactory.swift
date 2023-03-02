//
//  DataLayerDependencies.swift
//  iosApp
//
//  Created by GERARD on 02/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

@MainActor
class ViewModelFactory: ObservableObject {
    private var agendaRepository: AgendaRepository
    private var userRepository: UserRepository
    private var eventRepository: EventRepository

    init() {
        let platform = Platform(context: IOSContext())
        let db = DatabaseWrapper().createDb()
        let api = ConferenceApi.companion.create(
            platform: platform,
            baseUrl: Configuration.baseURL.absoluteString,
            enableNetworkLogs: isInDebugMode
        )
        let settings = AppleSettings(delegate: UserDefaults.standard)
        self.agendaRepository = AgendaRepositoryImpl(
            api: api,
            scheduleDao: ScheduleDao(db: db, settings: settings),
            speakerDao: SpeakerDao(db: db),
            talkDao: TalkDao(db: db),
            eventDao: EventDao(db: db, settings: settings),
            partnerDao: PartnerDao(db: db),
            featuresDao: FeaturesActivatedDao(db: db),
            qrCodeGenerator: QrCodeGeneratoriOS()
        )
        self.userRepository = UserRepositoryImpl(
            userDao: UserDao(db: db, platform: platform),
            eventDao: EventDao(db: db, settings: settings),
            qrCodeGenerator: QrCodeGeneratoriOS()
        )
        self.eventRepository = EventRepositoryImpl(
            api: api,
            eventDao: EventDao(db: db, settings: settings)
        )
    }

    func makeAppViewModel() -> AppViewModel {
        return AppViewModel(repository: self.eventRepository)
    }
    
    func makeEventListViewModel() -> EventListViewModel {
        return EventListViewModel(repository: self.eventRepository)
    }
    
    func makeHomeViewModel() -> HomeViewModel {
        return HomeViewModel(repository: self.agendaRepository)
    }
    
    func makeAgendaViewModel() -> AgendaViewModel {
        return AgendaViewModel(repository: self.agendaRepository)
    }
    
    func makeScheduleItemViewModel() -> ScheduleItemViewModel {
        return ScheduleItemViewModel(repository: self.agendaRepository)
    }
    
    func makeSpeakerViewModel() -> SpeakerViewModel {
        return SpeakerViewModel(repository: self.agendaRepository)
    }
    
    func makeNetworkingViewModel() -> NetworkingViewModel {
        return NetworkingViewModel(repository: self.userRepository)
    }
    
    func makePartnersViewModel() -> PartnersViewModel {
        return PartnersViewModel(repository: self.agendaRepository)
    }
    
    func makePartnerDetailViewModel(partnerId: String) -> PartnerDetailViewModel {
        return PartnerDetailViewModel(repository: self.agendaRepository, partnerId: partnerId)
    }

    func makeEventViewModel() -> EventViewModel {
        return EventViewModel(agendaRepository: self.agendaRepository)
    }
    
    func makeMenusViewModel() -> MenusViewModel {
        return MenusViewModel(repository: self.agendaRepository)
    }
}
