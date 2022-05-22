//
//  PartnersViewModel.swift
//  iosApp
//
//  Created by GERARD on 22/05/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import shared

enum PartnersUiState {
    case loading
    case success(PartnerGroupsUi)
    case failure
}

class PartnersViewModel: ObservableObject {
    let repository: AgendaRepository

    init(repository: AgendaRepository) {
        self.repository = repository
    }

    @Published var uiState: PartnersUiState = PartnersUiState.loading
    
    func fetchPartners() {
        repository.startCollectPartners(
            success: { partners in
                self.uiState = PartnersUiState.success(partners)
            },
            failure: { throwable in
                self.uiState = PartnersUiState.failure
            }
        )
    }
    
    func stop() {
        repository.stopCollectPartners()
    }
}
