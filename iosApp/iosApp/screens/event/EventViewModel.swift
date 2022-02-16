//
//  EventViewModel.swift
//  iosApp
//
//  Created by GERARD on 10/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import shared

enum EventUiState {
    case loading
    case success(EventUi)
    case failure(Error)
}

class EventViewModel: ObservableObject {
    let repository: AgendaRepository

    init(repository: AgendaRepository) {
        self.repository = repository
    }

    @Published var uiState: EventUiState = EventUiState.loading
    
    func fetchEvent() {
        repository.event { eventUi, error in
            if (eventUi != nil) {
                self.uiState = EventUiState.success(eventUi!)
            } else {
                self.uiState = EventUiState.failure(error!)
            }
        }
    }
}
