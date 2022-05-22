//
//  EventVM.swift
//  iosApp
//
//  Created by GERARD on 10/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import CodeScanner
import shared

enum EventNavigationState: Equatable {
    case none
    case menus
    case ticket
}

struct EventVM: View {
    @State private var navigationState = EventNavigationState.none
    @ObservedObject var viewModel: EventViewModel
    let agendaRepository: AgendaRepository
    @Environment(\.openURL) var openURL
    
    init(agendaRepository: AgendaRepository) {
        self.agendaRepository = agendaRepository
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
                            ticket: {
                                NavigationLink(isActive: Binding.constant(self.navigationState == .ticket)) {
                                    CodeScannerView(codeTypes: [.qr]) { response in
                                        if case let .success(result) = response {
                                            if (result.string != "") {
                                                viewModel.saveTicket(barcode: result.string)
                                                self.navigationState = EventNavigationState.none
                                            }
                                        }
                                    }
                                } label: {
                                    EmptyView()
                                }
                                .accessibility(hidden: true)
                                ButtonView(text: NSLocalizedString("actionTicketScanner", comment: "")) {
                                    self.navigationState = .ticket
                                }
                                .onAppear {
                                    self.navigationState = EventNavigationState.none
                                }
                            },
                            menus: {
                                NavigationLink(isActive: Binding.constant(self.navigationState == .menus)) {
                                    MenusVM(agendaRepository: self.agendaRepository)
                                } label: {
                                    EmptyView()
                                }
                                .accessibility(hidden: true)
                                ButtonView(text: NSLocalizedString("actionMenus", comment: "")) {
                                    self.navigationState = .menus
                                }
                                .onAppear {
                                    self.navigationState = EventNavigationState.none
                                }
                            },
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
                }
            }
            .navigationTitle(Text("screenEvent"))
            .navigationBarTitleDisplayMode(.inline)
        }
        .onAppear {
            viewModel.fetchEvent()
        }
        .onDisappear {
            viewModel.stop()
        }
    }
}
