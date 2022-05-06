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
    @Environment(\.openURL) var openURL
    
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
                            onFaqClicked: { url in
                                if let url2 = URL(string: url) { openURL(url2) }
                            },
                            onCoCClicked: { url in
                                if let url2 = URL(string: url) { openURL(url2) }
                            },
                            onTwitterClicked: { url in
                                if let url2 = URL(string: url) { openURL(url2) }
                            },
                            onLinkedInClicked: { url in
                                if let url2 = URL(string: url) { openURL(url2) }
                            }
                        )
                    case .failure:
                        Text("textError")
                    case .loading:
                        Text("textLoading")
                        .onAppear {
                            viewModel.fetchEvent()
                        }
                }
            }
            .navigationTitle(Text("screenEvent"))
            .navigationBarTitleDisplayMode(.inline)
        }
    }
}
