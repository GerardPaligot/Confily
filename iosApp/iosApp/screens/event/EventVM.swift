//
//  EventVM.swift
//  iosApp
//
//  Created by GERARD on 10/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct EventVM: View {
    @ObservedObject var viewModel: EventViewModel
    let onDisconnectedClicked: () -> ()
    
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
                            barcodeScanned: { barcode in
                                await viewModel.saveTicket(barcode: barcode)
                            },
                            onDisconnectedClicked: onDisconnectedClicked
                        )
                    case .failure:
                        Text("textError")
                    case .loading:
                        Text("textLoading")
                }
            }
        }
        .onAppear {
            viewModel.fetchEvent()
        }
        .onDisappear {
            viewModel.stop()
        }
    }
}
