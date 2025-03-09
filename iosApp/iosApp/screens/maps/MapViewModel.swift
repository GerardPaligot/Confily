//
//  MapViewModel.swift
//  iosApp
//
//  Created by Gérard PALIGOT on 09/03/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import Foundation
import SharedDi
import KMPNativeCoroutinesAsync

enum MapUiState {
    case loading
    case success(Array<MapFilledEntity>)
    case failure
}

@MainActor
class MapViewModel: ObservableObject {
    private let interactor: MapInteractor = InteractorHelper().mapInteractor
    
    @Published var uiState: MapUiState = MapUiState.loading
    private var task: Task<(), Never>?
    
    func fetchMaps() {
        task = Task {
            do {
                let stream = asyncSequence(for: interactor.mapsFilled())
                for try await maps in stream {
                    self.uiState = .success(maps)
                }
            } catch {
                self.uiState = .failure
            }
        }
    }

    func stop() {
        task?.cancel()
    }
}
