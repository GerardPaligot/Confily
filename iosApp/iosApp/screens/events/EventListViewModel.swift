//
//  EventListViewModel.swift
//  iosApp
//
//  Created by GERARD on 16/01/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SharedDi
import KMPNativeCoroutinesAsync

enum EventListUiState {
    case loading
    case success(EventItemListUi)
    case failure
}

@MainActor
class EventListViewModel: ObservableObject {
    private let interactor: EventInteractor = InteractorHelper().eventInteractor

    @Published var uiState: EventListUiState = EventListUiState.loading

    private var eventListTask: Task<(), Never>?

    func fetchEventList() {
        eventListTask = Task {
            if (await asyncError(for: interactor.fetchAndStoreEventList())) != nil {
                // ignored
            }
            do {
                let stream = asyncSequence(for: interactor.events())
                for try await events in stream {
                    self.uiState = .success(events)
                }
            } catch {
                self.uiState = .failure
            }
        }
    }

    func stop() {
        eventListTask?.cancel()
    }
}
