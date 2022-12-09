//
//  AgendaVM.swift
//  iosApp
//
//  Created by GERARD on 07/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

enum NavigationState: Equatable {
    case none
    case talk(String)
}

struct AgendaVM: View {
    @ObservedObject var viewModel: AgendaViewModel
    @State private var navigationState = NavigationState.none
    let agendaRepository: AgendaRepository
    
    init(agendaRepository: AgendaRepository) {
        self.agendaRepository = agendaRepository
        self.viewModel = AgendaViewModel(repository: agendaRepository)
    }

    var body: some View {
        let uiState = viewModel.uiState
        NavigationView {
            Group {
                switch uiState {
                    case .success(let agenda):
                        Agenda(
                            agenda: agenda,
                            onFilteringClicked: {
                                Task {
                                    viewModel.toggleFavoriteFiltering()
                                }
                            },
                            talkItem: { talk in
                                if (!talk.isPause) {
                                    NavigationLink(isActive: Binding.constant(self.navigationState == .talk(talk.id))) {
                                        ScheduleDetailVM(
                                            agendaRepository: self.agendaRepository,
                                            scheduleId: talk.id,
                                            speakerItem: { speaker in
                                                SpeakerItemNavigation(agendaRepository: agendaRepository, speaker: speaker)
                                            }
                                        )
                                    } label: {
                                        EmptyView()
                                    }
                                    .accessibility(hidden: true)
                                    TalkItemView(
                                        talk: talk,
                                        onFavoriteClicked: { talkItem in
                                            Task {
                                                await viewModel.markAsFavorite(talkItem: talkItem)
                                            }
                                        }
                                    )
                                        .onTapGesture {
                                            self.navigationState = NavigationState.talk(talk.id)
                                        }
                                        .onAppear {
                                            self.navigationState = NavigationState.none
                                        }
                                } else {
                                    PauseView()
                                }
                            }
                        )
                    case .failure:
                        Text("textError")
                    case .loading:
                        Agenda(
                            agenda: AgendaUi(onlyFavorites: false, talks: [:]),
                            onFilteringClicked: {},
                            talkItem: { talk in }
                        )
                }
            }
        }
        .onAppear {
            viewModel.fetchAgenda()
        }
        .onDisappear {
            viewModel.stop()
        }
    }
}
