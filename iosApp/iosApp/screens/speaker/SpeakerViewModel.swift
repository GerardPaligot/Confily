//
//  SpeakerDetailViewModel.swift
//  iosApp
//
//  Created by GERARD on 17/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import shared
import KMPNativeCoroutinesAsync

enum SpeakerUiState {
    case loading
    case success(SpeakerUi)
    case failure
}

@MainActor
class SpeakerViewModel: ObservableObject {
    let repository: AgendaRepository
    let speakerId: String
    let alarmScheduler: AlarmScheduler

    init(repository: AgendaRepository, speakerId: String) {
        self.repository = repository
        self.speakerId = speakerId
        self.alarmScheduler = AlarmScheduler(repository: repository)
    }

    @Published var uiState: SpeakerUiState = SpeakerUiState.loading
    
    private var speakerTask: Task<(), Never>?
    
    func fetchSpeakerDetails() {
        speakerTask = Task {
            do {
                let stream = asyncStream(for: repository.speakerNative(speakerId: self.speakerId))
                for try await speaker in stream {
                    self.uiState = .success(speaker)
                }
            } catch {
                self.uiState = .failure
            }
        }
    }

    func markAsFavorite(talkItem: TalkItemUi) async {
        await self.alarmScheduler.schedule(talkItem: talkItem)
    }
    
    func stop() {
        speakerTask?.cancel()
    }
}
