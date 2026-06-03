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
    private let interactor: EventInteractor = InteractorHelper().eventInteractor

    private var task: Task<(), Never>?
    private var featureFlagsTask: Task<(), Never>?

    @Published var hasNetworking: Bool = false
    @Published var hasQuiz: Bool = false

    func fetchAgenda() {
        task = Task {
            if let error = await asyncError(for: interactor.fetchAndStoreAgenda()) {
                // ignore
                print("Failed with error: \(error)")
            }
        }
    }

    func fetchFeatureFlags() {
        featureFlagsTask = Task {
            do {
                let stream = asyncSequence(for: interactor.featureFlags())
                for try await flags in stream {
                    self.hasNetworking = flags.hasNetworking
                    self.hasQuiz = flags.hasQuiz
                }
            } catch {
                print("Failed to fetch feature flags: \(error)")
            }
        }
    }

    func stop() {
        task?.cancel()
        featureFlagsTask?.cancel()
    }
}
