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

    init(
        agendaRepository: AgendaRepository,
        userRepository: UserRepository,
        eventRepository: EventRepository
    ) {
        self.agendaRepository = agendaRepository
        self.userRepository = userRepository
        self.eventRepository = eventRepository
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
