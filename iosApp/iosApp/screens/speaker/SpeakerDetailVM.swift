//
//  SpeakerDetailVM.swift
//  iosApp
//
//  Created by GERARD on 17/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

enum NavigationSpeakerState: Equatable {
    case none
    case talk(String)
}

struct SpeakerDetailVM: View {
    @ObservedObject var viewModel: SpeakerViewModel
    @State private var navigationState = NavigationSpeakerState.none
    @Environment(\.openURL) var openURL
    let agendaRepository: AgendaRepository
    let speakerId: String
    
    init(agendaRepository: AgendaRepository, speakerId: String) {
        self.agendaRepository = agendaRepository
        self.viewModel = SpeakerViewModel(repository: agendaRepository)
        self.speakerId = speakerId
    }

    var body: some View {
        let uiState = viewModel.uiState
        Group {
            switch uiState {
                case .success(let speakerUi):
                    SpeakerDetail(
                        speaker: speakerUi,
                        onTwitterClicked: { url in
                            if let url2 = URL(string: url) { openURL(url2) }
                        },
                        onGitHubClicked: { url in
                            if let url2 = URL(string: url) { openURL(url2) }
                        },
                        talkItem: { talk in
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
                                    self.navigationState = NavigationSpeakerState.talk(talk.id)
                                }
                                .onAppear {
                                    self.navigationState = NavigationSpeakerState.none
                                }
                        }
                    )
                case .failure:
                    Text("textError")
                case .loading:
                    Text("textLoading")
            }
        }
        .onAppear {
            viewModel.fetchSpeakerDetails(speakerId: speakerId)
        }
        .onDisappear {
            viewModel.stop()
        }
    }
}
