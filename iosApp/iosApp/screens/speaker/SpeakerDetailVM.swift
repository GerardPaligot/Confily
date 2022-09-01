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
    @Environment(\.openURL) var openURL
    let speakerId: String
    
    init(agendaRepository: AgendaRepository, speakerId: String) {
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
