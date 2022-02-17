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
    case failure(Error)
}

class SpeakerViewModel: ObservableObject {
    let repository: AgendaRepository

    init(repository: AgendaRepository) {
        self.repository = repository
    }

    @Published var uiState: SpeakerUiState = SpeakerUiState.loading
    
    func fetchSpeakerDetails(speakerId: String) {
        repository.speaker(speakerId: speakerId) { speakerUi, error in
            if (speakerUi != nil) {
                self.uiState = SpeakerUiState.success(speakerUi!)
            } else {
                self.uiState = SpeakerUiState.failure(error!)
            }
        }
    }
}
