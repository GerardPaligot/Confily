//
//  EventListVM.swift
//  iosApp
//
//  Created by GERARD on 16/01/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct EventListVM: View {
    @ObservedObject var viewModel: EventListViewModel
    let onEventClicked: (String) -> ()
    
    var body: some View {
        let uiState = viewModel.uiState
        NavigationView {
            Group {
                switch uiState {
                    case .success(let events):
                        EventListView(
                            events: events,
                            onEventClicked: onEventClicked
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
            viewModel.fetchEventList()
        }
        .onDisappear {
            viewModel.stop()
        }
    }
}
