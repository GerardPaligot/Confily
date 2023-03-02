//
//  AppView.swift
//  iosApp
//
//  Created by GERARD on 16/01/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct AppView: View {
    @EnvironmentObject var viewModelFactory: ViewModelFactory
    @ObservedObject var viewModel: AppViewModel
    
    init(viewModel: AppViewModel) {
        self.viewModel = viewModel
    }
    
    var body: some View {
        let uiState = viewModel.uiState
        Group {
            switch uiState {
                case .events:
                    EventListVM(
                        viewModel: viewModelFactory.makeEventListViewModel(),
                        eventItem: { event in
                            Button {
                                viewModel.saveEventId(eventId: event.id)
                            } label: {
                                EventItemView(item: event)
                                    .foregroundColor(Color.c4hOnSurface)
                            }
                        }
                    )
                case .agenda:
                    HomeView(
                        viewModel: viewModelFactory.makeHomeViewModel(),
                        onDisconnectedClicked: {
                            viewModel.disconnect()
                        }
                    )
                case .loading:
                    Text("textLoading")
            }
        }
        .onAppear {
            viewModel.fetchEventId()
        }
    }
}
