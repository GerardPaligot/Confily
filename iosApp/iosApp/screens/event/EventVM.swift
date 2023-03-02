//
//  EventVM.swift
//  iosApp
//
//  Created by GERARD on 10/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import CodeScanner

enum EventNavigationState: Equatable {
    case none
    case menus
    case ticket
}

struct EventVM: View {
    @EnvironmentObject var viewModelFactory: ViewModelFactory
    @State private var navigationState = EventNavigationState.none
    @ObservedObject var viewModel: EventViewModel
    let onDisconnectedClicked: () -> ()
    @Environment(\.openURL) var openURL
    
    init(viewModel: EventViewModel, onDisconnectedClicked: @escaping () -> ()) {
        self.onDisconnectedClicked = onDisconnectedClicked
        self.viewModel = viewModel
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
                                                Task {
                                                    await viewModel.saveTicket(barcode: result.string)
                                                    self.navigationState = EventNavigationState.none
                                                }
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
                                    MenusVM(viewModel: self.viewModelFactory.makeMenusViewModel())
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
                            onLinkClicked: { url in
                                if let url2 = URL(string: url) { openURL(url2) }
                            },
                            onMapClicked: { url in
                                if UIApplication.shared.canOpenURL(url) {
                                    UIApplication.shared.open(url, options: [:], completionHandler: nil)
                                }
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
            .navigationBarItems(trailing:
                HStack {
                    Button(action: {
                        onDisconnectedClicked()
                    }, label: {
                        Image(systemName: "power")
                    })
                    .accessibilityLabel("actionPowerOff")
                }
            )
        }
        .onAppear {
            viewModel.fetchEvent()
        }
        .onDisappear {
            viewModel.stop()
        }
    }
}
