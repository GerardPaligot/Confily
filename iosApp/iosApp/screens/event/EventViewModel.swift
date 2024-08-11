//
//  EventViewModel.swift
//  iosApp
//
//  Created by GERARD on 10/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import SharedDi
import KMPNativeCoroutinesAsync

enum EventUiState {
    case loading
    case success(EventUi)
    case failure
}

@MainActor
class EventViewModel: ObservableObject {
    private let agendaRepository: AgendaRepository = RepositoryHelper().agendaRepository

    @Published var uiState: EventUiState = EventUiState.loading

    private var eventTask: Task<(), Never>?

    func fetchEvent() {
        eventTask = Task {
            do {
                let stream = asyncSequence(for: agendaRepository.event())
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
        if let error = await asyncError(for: agendaRepository.insertOrUpdateTicket(barcode: barcode)) {
            // ignore
        }
    }
}
