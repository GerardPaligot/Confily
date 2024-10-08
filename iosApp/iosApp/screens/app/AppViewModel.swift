//
//  HomeViewModel.swift
//  iosApp
//
//  Created by GERARD on 16/01/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SharedDi
import KMPNativeCoroutinesAsync

enum AppUiState {
    case loading
    case events
    case agenda
}

@MainActor
class AppViewModel: ObservableObject {
    private let repository: EventRepository = RepositoryHelper().eventRepository

    @Published var uiState: AppUiState = AppUiState.loading

    func fetchEventId() {
        let defaultEvent = Bundle.main.object(forInfoDictionaryKey: "DEFAULT_EVENT") as! String
        self.uiState = repository.isInitialized(defaultEvent: defaultEvent) ? .agenda : .events
    }

    func saveEventId(eventId: String) {
        repository.saveEventId(eventId: eventId)
        self.uiState = .agenda
    }

    func disconnect() {
        self.uiState = .events
    }
}
