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

struct EventVM: View {
    @State private var isPresentingScanner = false
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
            .navigationBarItems(trailing:
                HStack {
                    Button(action: {
                        isPresentingScanner = true
                    }, label: {
                        Image(systemName: "tag")
                    })
                }
            )
            .sheet(isPresented: $isPresentingScanner) {
                CodeScannerView(codeTypes: [.qr]) { response in
                    if case let .success(result) = response {
                        if (result.string != "") {
                            viewModel.saveTicket(barcode: result.string)
                            isPresentingScanner = false
                        }
                    }
                }
            }
        }
    }
}
