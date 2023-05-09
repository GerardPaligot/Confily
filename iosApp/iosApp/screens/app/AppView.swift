//
//  AppView.swift
//  iosApp
//
//  Created by GERARD on 16/01/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct DeeplinkState {
    var isPresentingPartner: Binding<Bool>
    var partnerId: String?
}

struct AppView: View {
    @EnvironmentObject var viewModelFactory: ViewModelFactory
    @ObservedObject var viewModel: AppViewModel
    @State private var deeplinkState = DeeplinkState(
        isPresentingPartner: Binding.constant(false),
        partnerId: nil
    )
    
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
                        onEventClicked: { eventId in
                            viewModel.saveEventId(eventId: eventId)
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
        .onOpenURL { url in
            if (url.absoluteString.starts(with: "c4h://partners/")) {
                self.deeplinkState = DeeplinkState(
                    isPresentingPartner: Binding.constant(true),
                    partnerId: url.pathComponents[1]
                )
            }
        }
        .sheet(isPresented: self.deeplinkState.isPresentingPartner) {
            PartnerDetailVM(
                viewModel: self.viewModelFactory.makePartnerDetailViewModel(partnerId: self.deeplinkState.partnerId!)
            )
        }
    }
}
