//
//  AgendaVM.swift
//  iosApp
//
//  Created by GERARD on 07/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct AgendaVM: View {
    @ObservedObject var viewModel: AgendaViewModel
    var onTalkClicked: (_: String) -> ()

    var body: some View {
        let uiState = viewModel.uiState
        switch uiState {
            case .success(let agenda):
                Agenda(
                    agenda: agenda,
                    onTalkClicked: onTalkClicked,
                    onFavoriteClicked: { id, isFavorite in
                        viewModel.markAsFavorite(scheduleId: id, isFavorite: isFavorite)
                    }
                )
                .onDisappear {
                    viewModel.stop()
                }
            case .loading:
                Agenda(
                    agenda: AgendaUi(talks: [:]),
                    onTalkClicked: { String in },
                    onFavoriteClicked: { String, Bool in }
                )
                .onAppear {
                    viewModel.fetchAgenda()
                }
        }
    }
}
