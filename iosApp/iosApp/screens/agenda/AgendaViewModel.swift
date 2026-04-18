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
    private let repository: SessionRepository = RepositoryHelper().sessionRepository
    private let interactor: SessionInteractor = InteractorHelper().sessionInteractor
    private let alarmScheduler: AlarmScheduler = AlarmScheduler()

    @Published var uiState: AgendaUiState = AgendaUiState.loading
    @Published var isCurrentDay: Bool = false
    @Published var currentTimeSlotKey: String?
    
    private var agendaTask: Task<(), Never>?
    private var scrollToNowTask: Task<(), Never>?

    func fetchAgenda() {
        agendaTask = Task {
            do {
                let stream = asyncSequence(for: interactor.sessions())
                for try await agenda in stream {
                    if (!agenda.isEmpty) {
                        self.uiState = AgendaUiState.success(agenda)
                    }
                }
            } catch {
                self.uiState = AgendaUiState.failure
            }
        }
        scrollToNowTask = Task {
            do {
                let stream = asyncSequence(for: interactor.scrollToNow())
                for try await scrollToNow in stream {
                    self.isCurrentDay = scrollToNow.isCurrentDay
                    self.currentTimeSlotKey = scrollToNow.currentTimeSlotKey
                }
            } catch {
                // ignore
            }
        }
    }
    
    func stop() {
        agendaTask?.cancel()
        scrollToNowTask?.cancel()
    }
    
    func markAsFavorite(talkItem: TalkItemUi) async {
        await self.alarmScheduler.schedule(talkItem: talkItem)
    }
}
