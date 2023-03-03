//
//  AgendaViewModel.swift
//  iosApp
//
//  Created by GERARD on 07/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared
import KMPNativeCoroutinesAsync

enum AgendaUiState {
    case loading
    case success([String: AgendaUi])
    case failure
}

@MainActor
class AgendaViewModel: ObservableObject {
    let repository: AgendaRepository
    let alarmScheduler: AlarmScheduler

    init(repository: AgendaRepository) {
        self.repository = repository
        self.alarmScheduler = AlarmScheduler(repository: repository)
    }

    @Published var uiState: AgendaUiState = AgendaUiState.loading
    
    private var agendaTask: Task<(), Never>?
    
    func toggleFavoriteFiltering() {
        repository.toggleFavoriteFiltering()
    }

    func fetchAgenda() {
        agendaTask = Task {
            do {
                let stream = asyncStream(for: repository.agendaNative())
                for try await agenda in stream {
                    self.uiState = AgendaUiState.success(agenda)
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
