//
//  EventViewModel.swift
//  iosApp
//
//  Created by GERARD on 10/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import shared
import KMPNativeCoroutinesAsync

enum EventUiState {
    case loading
    case success(EventUi)
    case failure
}

@MainActor
class EventViewModel: ObservableObject {
    let agendaRepository: AgendaRepository

    init(agendaRepository: AgendaRepository) {
        self.agendaRepository = agendaRepository
    }

    @Published var uiState: EventUiState = EventUiState.loading
    
    private var eventTask: Task<(), Never>?
    
    func fetchEvent() {
        eventTask = Task {
            do {
                let stream = asyncStream(for: agendaRepository.eventNative())
                for try await event in stream {
                    self.uiState = .success(event)
                }
            } catch {
                self.uiState = .failure
            }
        }
    }
    
    func stop() {
        eventTask?.cancel()
    }
    
    func saveTicket(barcode: String) async {
        do {
            try await agendaRepository.insertOrUpdateTicket(barcode: barcode)
        } catch {
            // ignored
        }
    }
}
