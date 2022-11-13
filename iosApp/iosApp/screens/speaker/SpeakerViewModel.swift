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

    init(repository: AgendaRepository) {
        self.repository = repository
    }

    @Published var uiState: SpeakerUiState = SpeakerUiState.loading
    
    private var speakerTask: Task<(), Never>?
    
    func fetchSpeakerDetails(speakerId: String) {
        speakerTask = Task {
            do {
                let stream = asyncStream(for: repository.speakerNative(speakerId: speakerId))
                for try await speaker in stream {
                    self.uiState = .success(speaker)
                }
            } catch {
                self.uiState = .failure
            }
        }
    }
    
    func stop() {
        speakerTask?.cancel()
    }
}
