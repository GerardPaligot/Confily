//
//  HomeViewModel.swift
//  iosApp
//
//  Created by GERARD on 16/01/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared
import KMPNativeCoroutinesAsync

enum AppUiState {
    case loading
    case events
    case agenda
}

@MainActor
class AppViewModel: ObservableObject {
    let repository: EventRepository
    
    init(repository: EventRepository) {
        self.repository = repository
    }
    
    @Published var uiState: AppUiState = AppUiState.loading
    
    func fetchEventId() {
        self.uiState = repository.isInitialized() ? .agenda : .events
    }
    
    func saveEventId(eventId: String) {
        repository.saveEventId(eventId: eventId)
        self.uiState = .agenda
    }
    
    func disconnect() {
        self.uiState = .events
    }
}
