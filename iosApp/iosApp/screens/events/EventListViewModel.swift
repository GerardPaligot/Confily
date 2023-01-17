//
//  EventListViewModel.swift
//  iosApp
//
//  Created by GERARD on 16/01/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared
import KMPNativeCoroutinesAsync

enum EventListUiState {
    case loading
    case success(EventItemListUi)
    case failure
}

@MainActor
class EventListViewModel: ObservableObject {
    let repository: EventRepository
    
    init(repository: EventRepository) {
        self.repository = repository
    }
    
    @Published var uiState: EventListUiState = EventListUiState.loading
    
    private var eventListTask: Task<(), Never>?
    
    func fetchEventList() {
        repository.fetchAndStoreEventList { _ in
        }
        eventListTask = Task {
            do {
                let stream = asyncStream(for: repository.eventsNative())
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
