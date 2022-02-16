//
//  EventVM.swift
//  iosApp
//
//  Created by GERARD on 10/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct EventVM: View {
    @ObservedObject var viewModel: EventViewModel
    
    init(agendaRepository: AgendaRepository) {
        self.viewModel = EventViewModel(repository: agendaRepository)
    }

    var body: some View {
        let uiState = viewModel.uiState
        NavigationView {
            Group {
                switch uiState {
                    case .success(let eventUi):
                        Event(
                            event: eventUi,
                            onFaqClicked: { String in },
                            onCoCClicked: { String in },
                            onTwitterClicked: { String in },
                            onLinkedInClicked: { String in }
                        )
                    case .failure(_):
                        Text("Something wrong happened")
                    case .loading:
                        Text("Loading")
                        .onAppear {
                            viewModel.fetchEvent()
                        }
                }
            }
            .navigationTitle(Text("Event Info"))
            .navigationBarTitleDisplayMode(.inline)
        }
    }
}
