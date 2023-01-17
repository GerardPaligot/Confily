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
    @ObservedObject var viewModel: AppViewModel
    private let agendaRepository: AgendaRepository
    private let userRepository: UserRepository
    private let eventRepository: EventRepository
    
    init(
        agendaRepository: AgendaRepository,
        userRepository: UserRepository,
        eventRepository: EventRepository
    ) {
        self.agendaRepository = agendaRepository
        self.userRepository = userRepository
        self.eventRepository = eventRepository
        self.viewModel = AppViewModel(repository: eventRepository)
    }
    
    var body: some View {
        let uiState = viewModel.uiState
        Group {
            switch uiState {
                case .events:
                    EventListVM(
                        repository: self.eventRepository,
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
                        agendaRepository: self.agendaRepository,
                        userRepository: self.userRepository,
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
