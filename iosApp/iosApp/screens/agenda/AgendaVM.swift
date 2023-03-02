//
//  AgendaVM.swift
//  iosApp
//
//  Created by GERARD on 07/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

enum NavigationState: Equatable {
    case none
    case talk(String)
}

struct AgendaVM: View {
    @EnvironmentObject var viewModelFactory: ViewModelFactory
    @ObservedObject var viewModel: AgendaViewModel
    @State private var navigationState = NavigationState.none
    
    init(viewModel: AgendaViewModel) {
        self.viewModel = viewModel
    }

    var body: some View {
        let uiState = viewModel.uiState
        NavigationView {
            Group {
                switch uiState {
                    case .success(let agendas):
                        Agenda(
                            agendas: agendas,
                            onFilteringClicked: {
                                Task {
                                    viewModel.toggleFavoriteFiltering()
                                }
                            },
                            talkItem: { talk in
                                if (!talk.isPause) {
                                    NavigationLink(isActive: Binding.constant(self.navigationState == .talk(talk.id))) {
                                        ScheduleDetailVM(
                                            viewModel: viewModelFactory.makeScheduleItemViewModel(),
                                            scheduleId: talk.id,
                                            speakerItem: { speaker in
                                                SpeakerItemNavigation(viewModel: viewModelFactory.makeSpeakerViewModel(), speaker: speaker)
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
                    case .loading(let agendas):
                        Agenda(
                            agendas: agendas,
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
