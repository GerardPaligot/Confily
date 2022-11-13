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
    case failure
}

@MainActor
class EventViewModel: ObservableObject {
    let repository: AgendaRepository

    init(repository: AgendaRepository) {
        self.repository = repository
    }

    @Published var uiState: EventUiState = EventUiState.loading
    
    func fetchEvent() {
        repository.startCollectEvent(
            success: { event in
                self.uiState = EventUiState.success(event)
            },
            failure: { throwable in
                self.uiState = EventUiState.failure
            }
        )
    }
    
    func stop() {
        repository.stopCollectEvent()
    }
    
    func saveTicket(barcode: String) async {
        do {
            try await repository.insertOrUpdateTicket(barcode: barcode)
        } catch {
            // ignored
        }
    }
}
