//
//  AgendaVM.swift
//  iosApp
//
//  Created by GERARD on 07/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct AgendaVM: View {
    @ObservedObject var viewModel: AgendaViewModel
    
    init(viewModel: AgendaViewModel) {
        self.viewModel = viewModel
    }

    var body: some View {
        let uiState = viewModel.uiState
        Group {
            switch uiState {
                case .success(let agendas):
                    Agenda(
                        agendas: agendas,
                        onFilteringClicked: {
                            Task {
                                viewModel.toggleFavoriteFiltering()
                            }
                        },
                        onFavoriteClicked: { talk in
                            Task {
                                await viewModel.markAsFavorite(talkItem: talk)
                            }
                        }
                    )
                case .failure:
                    Text("textError")
                case .loading:
                    Text("textLoading")
            }
        }
        .onAppear {
            viewModel.fetchAgenda()
        }
        .onDisappear {
            viewModel.stop()
        }
    }
}
