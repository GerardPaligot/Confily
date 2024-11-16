//
//  AppViewModel.swift
//  iosApp
//
//  Created by GERARD on 16/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import SharedDi
import KMPNativeCoroutinesAsync

@MainActor
class HomeViewModel: ObservableObject {
    private let repository: AgendaRepository = RepositoryHelper().agendaRepository

    private var task: Task<(), Never>?

    func fetchAgenda() {
        task = Task {
            if let error = await asyncError(for: repository.fetchAndStoreAgenda()) {
                // ignore
                print("Failed with error: \(error)")
            }
        }
    }

    func stop() {
        task?.cancel()
    }
}
