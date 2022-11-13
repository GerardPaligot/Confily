//
//  AppViewModel.swift
//  iosApp
//
//  Created by GERARD on 16/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import shared

@MainActor
class AppViewModel: ObservableObject {
    let repository: AgendaRepository

    init(repository: AgendaRepository) {
        self.repository = repository
    }

    func fetchAgenda() async {
        do {
            try await repository.fetchAndStoreAgenda()
        } catch {
            // ignored
        }
    }
}
