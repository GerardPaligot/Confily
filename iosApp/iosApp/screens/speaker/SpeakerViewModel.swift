//
//  SpeakerDetailViewModel.swift
//  iosApp
//
//  Created by GERARD on 17/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import shared

enum SpeakerUiState {
    case loading
    case success(SpeakerUi)
    case failure
}

@MainActor
class SpeakerViewModel: ObservableObject {
    let repository: AgendaRepository

    init(repository: AgendaRepository) {
        self.repository = repository
    }

    @Published var uiState: SpeakerUiState = SpeakerUiState.loading
    
    func fetchSpeakerDetails(speakerId: String) {
        repository.startCollectSpeaker(
            speakerId: speakerId,
            success: { speaker in
                self.uiState = SpeakerUiState.success(speaker)
            },
            failure: { throwable in
                self.uiState = SpeakerUiState.failure
            }
        )
    }
    
    func stop() {
        repository.stopCollectSpeaker()
    }
}
