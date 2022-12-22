//
//  SpeakerDetail.swift
//  iosApp
//
//  Created by GERARD on 17/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct SpeakerDetail<TalkItem: View>: View {
    var speaker: SpeakerUi
    var onTwitterClicked: (_: String) -> ()
    var onGitHubClicked: (_: String) -> ()
    let talkItem: (TalkItemUi) -> TalkItem
    
    init(
        speaker: SpeakerUi,
        onTwitterClicked: @escaping (_: String) -> (),
        onGitHubClicked: @escaping (_: String) -> (),
        @ViewBuilder talkItem: @escaping (TalkItemUi) -> TalkItem
    ) {
        self.speaker = speaker
        self.onTwitterClicked = onTwitterClicked
        self.onGitHubClicked = onGitHubClicked
        self.talkItem = talkItem
    }
    
    var body: some View {
        ScrollView {
            LazyVStack(spacing: 8) {
                SpeakerHeaderView(
                    url: speaker.url,
                    name: speaker.name,
                    company: speaker.company
                )
                SpeakerSectionView(
                    speaker: speaker,
                    onTwitterClicked: onTwitterClicked,
                    onGitHubClicked: onGitHubClicked
                )
                Spacer().frame(maxHeight: 24)
                ForEach(speaker.talks, id: \.id) { talk in
                    self.talkItem(talk)
                }
            }
            .padding(.horizontal, 16)
        }
    }
}

struct SpeakerDetail_Previews: PreviewProvider {
    static var previews: some View {
        SpeakerDetail(
            speaker: SpeakerUi.companion.fake,
            onTwitterClicked: { String in },
            onGitHubClicked: { String in },
            talkItem: { talk in
                TalkItemView(
                    talk: talk,
                    onFavoriteClicked: { TalkItemUi in }
                )
            }
        )
    }
}
