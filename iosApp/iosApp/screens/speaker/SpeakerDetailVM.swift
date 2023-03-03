//
//  SpeakerDetailVM.swift
//  iosApp
//
//  Created by GERARD on 17/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct SpeakerDetailVM: View {
    @ObservedObject var viewModel: SpeakerViewModel

    var body: some View {
        let uiState = viewModel.uiState
        Group {
            switch uiState {
                case .success(let speakerUi):
                    SpeakerDetail(
                        speaker: speakerUi,
                        onFavoriteClicked: { talk in
                            Task {
                                await viewModel.markAsFavorite(talkItem: talk)
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
            viewModel.fetchSpeakerDetails()
        }
        .onDisappear {
            viewModel.stop()
        }
    }
}
