//
//  SpeakerDetailViewModel.swift
//  iosApp
//
//  Created by GERARD on 17/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import SharedDi
import KMPNativeCoroutinesAsync

enum SpeakerUiState {
    case loading
    case success(SpeakerUi)
    case failure
}

@MainActor
class SpeakerViewModel: ObservableObject {
    private let repository: AgendaRepository = RepositoryHelper().agendaRepository
    private let alarmScheduler: AlarmScheduler = AlarmScheduler()
    let speakerId: String

    init(speakerId: String) {
        self.speakerId = speakerId
    }

    @Published var uiState: SpeakerUiState = SpeakerUiState.loading
    
    private var speakerTask: Task<(), Never>?
    
    func fetchSpeakerDetails() {
        speakerTask = Task {
            do {
                let stream = asyncSequence(for: repository.speaker(speakerId: self.speakerId))
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
