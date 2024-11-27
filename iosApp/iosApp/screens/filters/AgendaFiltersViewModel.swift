//
//  AgendaFiltersViewModel.swift
//  iosApp
//
//  Created by GERARD on 08/01/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SharedDi
import KMPNativeCoroutinesAsync

enum AgendaFiltersUiState {
    case loading
    case success(FiltersUi)
    case failure
}

@MainActor
class AgendaFiltersViewModel: ObservableObject {
    private let sessionRepository: SessionRepository = RepositoryHelper().sessionRepository
    private let sessionInteractor: SessionInteractor = InteractorHelper().sessionInteractor

    @Published var uiState: AgendaFiltersUiState = AgendaFiltersUiState.loading

    private var filtersTask: Task<(), Never>?

    func fetchFilters() {
        filtersTask = Task {
            do {
                let stream = asyncSequence(for: sessionInteractor.filters())
                for try await filters in stream {
                    self.uiState = .success(filters)
                }
            } catch {
                self.uiState = .failure
            }
        }
    }

    func stop() {
        filtersTask?.cancel()
    }

    func applyFavoriteFilter(selected: Bool) async {
        sessionRepository.applyFavoriteFilter(selected: selected)
    }
    
    func applyCategoryFilter(categoryUi: CategoryUi, selected: Bool) async {
        sessionRepository.applyCategoryFilter(categoryId: categoryUi.id, selected: selected)
    }
    
    func applyFormatFilter(formatUi: FormatUi, selected: Bool) async {
        sessionRepository.applyFormatFilter(formatId: formatUi.id, selected: selected)
    }
}
