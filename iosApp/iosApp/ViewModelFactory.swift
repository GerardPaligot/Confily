//
//  DataLayerDependencies.swift
//  iosApp
//
//  Created by GERARD on 02/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SharedDi

@MainActor
class ViewModelFactory: ObservableObject {
    func makeAppViewModel() -> AppViewModel {
        return AppViewModel()
    }

    func makeEventListViewModel() -> EventListViewModel {
        return EventListViewModel()
    }

    func makeHomeViewModel() -> HomeViewModel {
        return HomeViewModel()
    }

    func makeAgendaViewModel() -> AgendaViewModel {
        return AgendaViewModel()
    }

    func makeAgendaFiltersViewModel() -> AgendaFiltersViewModel {
        AgendaFiltersViewModel()
    }

    func makeScheduleItemViewModel(scheduleId: String) -> ScheduleItemViewModel {
        return ScheduleItemViewModel(scheduleId: scheduleId)
    }
    
    func makeScheduleEventSessionViewModel(scheduleId: String) -> ScheduleEventSessionViewModel {
        return ScheduleEventSessionViewModel(scheduleId: scheduleId)
    }

    func makeSpeakerViewModel(speakerId: String) -> SpeakerViewModel {
        return SpeakerViewModel(speakerId: speakerId)
    }

    func makeNetworkingViewModel() -> NetworkingViewModel {
        return NetworkingViewModel()
    }

    func makePartnersViewModel() -> PartnersViewModel {
        return PartnersViewModel()
    }

    func makePartnerDetailViewModel(partnerId: String) -> PartnerDetailViewModel {
        return PartnerDetailViewModel(partnerId: partnerId)
    }

    func makeEventViewModel() -> EventViewModel {
        return EventViewModel()
    }

    func makeMenusViewModel() -> MenusViewModel {
        return MenusViewModel()
    }
    
    func makeTeamMembersViewModel() -> TeamMembersViewModel {
        return TeamMembersViewModel()
    }
    
    func makeTeamMemberViewModel(memberId: String) -> TeamMemberViewModel {
        return TeamMemberViewModel(memberId: memberId)
    }
}
