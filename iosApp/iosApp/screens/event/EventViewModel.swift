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
    private let interactor: EventInteractor = InteractorHelper().eventInteractor

    @Published var uiState: EventUiState = EventUiState.loading

    private var eventTask: Task<(), Never>?

    func fetchEvent() {
        eventTask = Task {
            do {
                let stream = asyncSequence(for: interactor.event())
                for try await event in stream {
                    if (event != nil) {
                        self.uiState = .success(event!)
                    } else {
                        print("Nullpointer")
                    }
                }
            } catch {
                print(error)
                self.uiState = .failure
            }
        }
    }

    func stop() {
        eventTask?.cancel()
    }

    func saveTicket(barcode: String) async {
        if (await asyncError(for: interactor.insertOrUpdateTicket(barcode: barcode))) != nil {
            // ignore
        }
    }
}
