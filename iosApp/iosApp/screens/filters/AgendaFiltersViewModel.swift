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
    private let agendaRepository: AgendaRepository = RepositoryHelper().agendaRepository

    @Published var uiState: AgendaFiltersUiState = AgendaFiltersUiState.loading

    private var filtersTask: Task<(), Never>?

    func fetchFilters() {
        filtersTask = Task {
            do {
                let stream = asyncSequence(for: agendaRepository.filters())
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
        agendaRepository.applyFavoriteFilter(selected: selected)
    }
    
    func applyCategoryFilter(categoryUi: CategoryUi, selected: Bool) async {
        agendaRepository.applyCategoryFilter(categoryUi: categoryUi, selected: selected)
    }
    
    func applyFormatFilter(formatUi: FormatUi, selected: Bool) async {
        agendaRepository.applyFormatFilter(formatUi: formatUi, selected: selected)
    }
}
