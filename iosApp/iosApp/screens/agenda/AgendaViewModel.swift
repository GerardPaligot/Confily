//
//  AgendaViewModel.swift
//  iosApp
//
//  Created by GERARD on 07/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

enum AgendaUiState {
    case loading
    case success(AgendaUi)
}

class AgendaViewModel: ObservableObject {
    let repository: AgendaRepository

    init(repository: AgendaRepository) {
        self.repository = repository
    }

    @Published var uiState: AgendaUiState = AgendaUiState.loading
    
    func fetchAgenda() {
        repository.startCollectAgenda(success: { agenda in
            self.uiState = AgendaUiState.success(agenda)
        })
    }
    
    func stop() {
        repository.stopCollectAgenda()
    }
    
    func markAsFavorite(scheduleId: String, isFavorite: Bool) {
        repository.markAsRead(scheduleId: scheduleId, isFavorite: isFavorite, completionHandler: {_,_ in
        })
    }
}
