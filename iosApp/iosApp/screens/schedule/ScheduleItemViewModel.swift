//
//  ScheduleItemViewModel.swift
//  iosApp
//
//  Created by GERARD on 15/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import shared
import KMPNativeCoroutinesAsync

enum ScheduleUiState {
    case loading
    case success(TalkUi)
    case failure
}

@MainActor
class ScheduleItemViewModel: ObservableObject {
    let repository: AgendaRepository
    let scheduleId: String

    init(repository: AgendaRepository, scheduleId: String) {
        self.repository = repository
        self.scheduleId = scheduleId
    }

    @Published var uiState: ScheduleUiState = .loading
    
    private var scheduleTask: Task<(), Never>?

    func fetchScheduleDetails() {
        scheduleTask = Task {
            do {
                let stream = asyncStream(for: repository.scheduleItemNative(scheduleId: scheduleId))
                for try await schedule in stream {
                    self.uiState = .success(schedule)
                }
            } catch {
                self.uiState = .failure
            }
        }
    }

    func stop() {
        scheduleTask?.cancel()
    }
}
