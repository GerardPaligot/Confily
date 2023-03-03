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
    var onLinkClicked: (_: String) -> ()
    let talkItem: (TalkItemUi) -> TalkItem
    
    init(
        speaker: SpeakerUi,
        onLinkClicked: @escaping (_: String) -> (),
        @ViewBuilder talkItem: @escaping (TalkItemUi) -> TalkItem
    ) {
        self.speaker = speaker
        self.onLinkClicked = onLinkClicked
        self.talkItem = talkItem
    }
    
    var body: some View {
        ScrollView {
            LazyVStack(spacing: 16) {
                SocialHeaderView(
                    title: speaker.name,
                    // description: speaker.company,
                    logoUrl: speaker.url,
                    twitterUrl: speaker.twitterUrl,
                    githubUrl: speaker.githubUrl
                )
                Text(speaker.bio)
                    .font(Font.callout)
                    .foregroundColor(Color.c4hOnBackground)
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
            onLinkClicked: { String in },
            talkItem: { talk in
                TalkItemView(
                    talk: talk,
                    onFavoriteClicked: { TalkItemUi in }
                )
            }
        )
    }
}
