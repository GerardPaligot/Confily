//
//  PartnersViewModel.swift
//  iosApp
//
//  Created by GERARD on 22/05/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import SharedDi
import KMPNativeCoroutinesAsync

enum PartnersUiState {
    case loading
    case success(PartnersActivitiesUi)
    case failure
}

@MainActor
class PartnersViewModel: ObservableObject {
    private let interactor: PartnerInteractor = InteractorHelper().partnerInteractor

    @Published var uiState: PartnersUiState = PartnersUiState.loading

    private var partnersTask: Task<(), Never>?

    func fetchPartners() {
        partnersTask = Task {
            do {
                let stream = asyncSequence(for: interactor.partners())
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
