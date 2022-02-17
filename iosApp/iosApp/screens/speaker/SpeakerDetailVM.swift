//
//  SpeakerDetailVM.swift
//  iosApp
//
//  Created by GERARD on 17/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct SpeakerDetailVM: View {
    @ObservedObject var viewModel: SpeakerViewModel
    let speakerId: String
    let onTwitterClicked: (_: String) -> ()
    let onGitHubClicked: (_: String) -> ()
    
    init(agendaRepository: AgendaRepository, speakerId: String, twitterAction: @escaping (_: String) -> (), githubAction: @escaping (_: String) -> ()) {
        self.viewModel = SpeakerViewModel(repository: agendaRepository)
        self.speakerId = speakerId
        self.onTwitterClicked = twitterAction
        self.onGitHubClicked = githubAction
    }

    var body: some View {
        let uiState = viewModel.uiState
        Group {
            switch uiState {
                case .success(let speakerUi):
                    SpeakerDetail(
                        speaker: speakerUi,
                        onTwitterClicked: onTwitterClicked,
                        onGitHubClicked: onGitHubClicked
                    )
                case .failure(_):
                    Text("Something wrong happened")
                case .loading:
                    Text("Loading")
            }
        }
        .onAppear {
            viewModel.fetchSpeakerDetails(speakerId: speakerId)
        }
    }
}
