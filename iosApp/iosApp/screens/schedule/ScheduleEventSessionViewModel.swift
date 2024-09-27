//
//  ScheduleEventSessionViewModel.swift
//  iosApp
//
//  Created by GERARD on 10/05/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import SharedDi
import KMPNativeCoroutinesAsync

enum ScheduleEventSessionUiState {
    case loading
    case success(EventSessionItemUi)
    case failure
}

@MainActor
class ScheduleEventSessionViewModel: ObservableObject {
    private let repository: SchedulesRepository = RepositoryHelper().schedulesRepository
    let scheduleId: String

    init(scheduleId: String) {
        self.scheduleId = scheduleId
    }

    @Published var uiState: ScheduleEventSessionUiState = .loading

    private var scheduleTask: Task<(), Never>?

    func fetchScheduleDetails() {
        scheduleTask = Task {
            do {
                let stream = asyncSequence(for: self.repository.scheduleEventSessionItem(scheduleId: scheduleId))
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
