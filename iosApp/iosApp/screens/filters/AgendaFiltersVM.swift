//
//  AgendaFiltersVM.swift
//  iosApp
//
//  Created by GERARD on 08/01/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import SharedDi

struct AgendaFiltersVM: View {
    @ObservedObject var viewModel: AgendaFiltersViewModel
    @Environment(\.dismiss) private var dismiss
    
    init(viewModel: AgendaFiltersViewModel) {
        self.viewModel = viewModel
    }
    
    var body: some View {
        let uiState = viewModel.uiState
        NavigationView {
            Group {
                switch uiState {
                    case .success(let filtersUi):
                    AgendaFilters(
                        filtersUi: filtersUi,
                        onFavoriteSelected: { selected in
                            Task {
                                await viewModel.applyFavoriteFilter(selected: selected)
                            }
                        },
                        onCategorySelected: { category, selected in
                            Task {
                                await viewModel.applyCategoryFilter(categoryUi: category, selected: selected)
                            }
                        },
                        onFormatSelected: { format, selected in
                            Task {
                                await viewModel.applyFormatFilter(formatUi: format, selected: selected)
                            }
                        }
                    )
                    case .failure:
                        Text("textError")
                    case .loading:
                        Text("textLoading")
                }
            }
            .toolbar {
                ToolbarItem(placement: .confirmationAction) {
                    Button {
                        dismiss()
                    } label: {
                        Image(systemName: "xmark.circle.fill")
                            .foregroundStyle(.gray)
                    }
                }
            }
        }
        .modifier(PresentationDetentsModifier())
        .onAppear {
            viewModel.fetchFilters()
        }
        .onDisappear {
            viewModel.stop()
        }
    }
}

private struct PresentationDetentsModifier: ViewModifier {
    func body(content: Content) -> some View {
        if #available(iOS 16.0, *) {
            content.presentationDetents([.medium, .large])
        } else {
            content
        }
    }
}