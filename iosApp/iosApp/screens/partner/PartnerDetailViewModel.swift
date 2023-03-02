//
//  PartnerDetailViewModel.swift
//  iosApp
//
//  Created by GERARD on 30/12/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import shared
import KMPNativeCoroutinesAsync

enum PartnerUiState {
    case loading
    case success(PartnerItemUi)
    case failure
}

@MainActor
class PartnerDetailViewModel: ObservableObject {
    let repository: AgendaRepository
    let partnerId: String

    init(repository: AgendaRepository, partnerId: String) {
        self.repository = repository
        self.partnerId = partnerId
    }

    @Published var uiState: PartnerUiState = PartnerUiState.loading

    private var partnerTask: Task<(), Never>?

    func fetchPartner() {
        partnerTask = Task {
            do {
                let stream = asyncStream(for: repository.partnerNative(id: self.partnerId))
                for try await partner in stream {
                    self.uiState = PartnerUiState.success(partner)
                }
            } catch {
                self.uiState = PartnerUiState.failure
            }
        }
    }
    
    func stop() {
        partnerTask?.cancel()
    }
}
