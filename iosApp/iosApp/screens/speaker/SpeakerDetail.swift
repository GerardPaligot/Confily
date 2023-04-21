//
//  SpeakerDetail.swift
//  iosApp
//
//  Created by GERARD on 17/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct SpeakerDetail: View {
    let speaker: SpeakerUi
    let onFavoriteClicked: (TalkItemUi) -> ()
    
    init(
        speaker: SpeakerUi,
        onFavoriteClicked: @escaping (TalkItemUi) -> ()
    ) {
        self.speaker = speaker
        self.onFavoriteClicked = onFavoriteClicked
    }
    
    var body: some View {
        List {
            Section {
                HStack {
                    Spacer()
                    SocialHeaderView(
                        title: speaker.name,
                        pronouns: speaker.pronouns,
                        logoUrl: speaker.url,
                        twitterUrl: speaker.twitterUrl,
                        mastodonUrl: speaker.mastodonUrl,
                        linkedInUrl: speaker.linkedinUrl,
                        githubUrl: speaker.githubUrl,
                        websiteUrl: speaker.websiteUrl
                    )
                    Spacer()
                }
            }
            Section {
                if (speaker.jobTitle != nil) {
                    HStack(alignment: .center) {
                        Text("titleJob")
                        Spacer()
                        Text(speaker.jobTitle!)
                            .foregroundColor(.secondary)
                    }
                    .accessibilityElement(children: .combine)
                }
                if (speaker.company != nil) {
                    HStack(alignment: .center) {
                        Text("titleCompany")
                        Spacer()
                        Text(speaker.company!)
                            .foregroundColor(.secondary)
                    }
                    .accessibilityElement(children: .combine)
                }
                Text(speaker.bio)
                    .font(Font.callout)
            }
            Section {
                ForEach(speaker.talks, id: \.id) { talk in
                    TalkItemNavigation(
                        talk: talk,
                        onFavoriteClicked: self.onFavoriteClicked
                    )
                }
            } header: {
                Text("titleTalks")
            }
        }
        .listStyle(.grouped)
    }
}

struct SpeakerDetail_Previews: PreviewProvider {
    static var previews: some View {
        SpeakerDetail(
            speaker: SpeakerUi.companion.fake,
            onFavoriteClicked: { talk in }
        )
        .environmentObject(ViewModelFactory())
    }
}
