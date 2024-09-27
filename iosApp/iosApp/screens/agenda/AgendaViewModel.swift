//
//  AgendaViewModel.swift
//  iosApp
//
//  Created by GERARD on 07/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import SharedDi
import KMPNativeCoroutinesAsync

enum AgendaUiState {
    case loading
    case success([String: AgendaUi])
    case failure
}

@MainActor
class AgendaViewModel: ObservableObject {
    private let repository: SchedulesRepository = RepositoryHelper().schedulesRepository
    private let alarmScheduler: AlarmScheduler = AlarmScheduler()

    @Published var uiState: AgendaUiState = AgendaUiState.loading
    
    private var agendaTask: Task<(), Never>?

    func fetchAgenda() {
        agendaTask = Task {
            do {
                let stream = asyncSequence(for: repository.agenda())
                for try await agenda in stream {
                    if (!agenda.isEmpty) {
                        self.uiState = AgendaUiState.success(agenda)
                    }
                }
            } catch {
                self.uiState = AgendaUiState.failure
            }
        }
    }
    
    func stop() {
        agendaTask?.cancel()
    }
    
    func markAsFavorite(talkItem: TalkItemUi) async {
        await self.alarmScheduler.schedule(talkItem: talkItem)
    }
}
