//
//  SpeakerDetail.swift
//  iosApp
//
//  Created by GERARD on 17/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import SharedDi

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
                        title: speaker.info.name,
                        pronouns: speaker.info.pronouns,
                        logoUrl: speaker.info.url,
                        xUrl: speaker.info.socials.first(where: { $0.type == SocialTypeUi.x })?.url,
                        mastodonUrl: speaker.info.socials.first(where: { $0.type == SocialTypeUi.mastodon })?.url,
                        blueskyUrl: speaker.info.socials.first(where: { $0.type == SocialTypeUi.bluesky })?.url,
                        facebookUrl: speaker.info.socials.first(where: { $0.type == SocialTypeUi.facebook })?.url,
                        instagramUrl: speaker.info.socials.first(where: { $0.type == SocialTypeUi.instagram })?.url,
                        youtubeUrl: speaker.info.socials.first(where: { $0.type == SocialTypeUi.youtube })?.url,
                        linkedInUrl: speaker.info.socials.first(where: { $0.type == SocialTypeUi.linkedin })?.url,
                        websiteUrl: speaker.info.socials.first(where: { $0.type == SocialTypeUi.website })?.url,
                        emailUrl: speaker.info.socials.first(where: { $0.type == SocialTypeUi.email })?.url
                    )
                    Spacer()
                }
            }
            Section {
                if (speaker.info.jobTitle != nil) {
                    HStack(alignment: .center) {
                        Text("titleJob")
                        Spacer()
                        Text(speaker.info.jobTitle!)
                            .foregroundColor(.secondary)
                    }
                    .accessibilityElement(children: .combine)
                }
                if (speaker.info.company != nil) {
                    HStack(alignment: .center) {
                        Text("titleCompany")
                        Spacer()
                        Text(speaker.info.company!)
                            .foregroundColor(.secondary)
                    }
                    .accessibilityElement(children: .combine)
                }
                Text(speaker.info.bio)
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
