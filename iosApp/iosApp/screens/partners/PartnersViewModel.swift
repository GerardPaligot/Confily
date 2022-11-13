//
//  PartnersViewModel.swift
//  iosApp
//
//  Created by GERARD on 22/05/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import shared
import KMPNativeCoroutinesAsync

enum PartnersUiState {
    case loading
    case success(PartnerGroupsUi)
    case failure
}

@MainActor
class PartnersViewModel: ObservableObject {
    let repository: AgendaRepository

    init(repository: AgendaRepository) {
        self.repository = repository
    }

    @Published var uiState: PartnersUiState = PartnersUiState.loading

    private var partnersTask: Task<(), Never>?

    func fetchPartners() {
        partnersTask = Task {
            do {
                let stream = asyncStream(for: repository.partnersNative())
                for try await partners in stream {
                    self.uiState = PartnersUiState.success(partners)
                }
            } catch {
                self.uiState = PartnersUiState.failure
            }
        }
    }
    
    func stop() {
        partnersTask?.cancel()
    }
}
