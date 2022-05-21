//
//  SpeakerSectionView.swift
//  iosApp
//
//  Created by GERARD on 17/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct SpeakerSectionView: View {
    var speaker: SpeakerUi
    var bodyFont: Font = Font.callout
    var color: Color = Color.c4hOnBackground
    var onTwitterClicked: (_: String) -> ()
    var onGitHubClicked: (_: String) -> ()
    
    var body: some View {
        VStack(alignment: .leading) {
            if (speaker.twitter != nil || speaker.github != nil) {
                HStack {
                    if (speaker.twitter != nil) {
                        SocialItem(
                            iconName: "ic_twitter",
                            text: speaker.twitter!,
                            onClick: {
                                onTwitterClicked(speaker.twitterUrl!)
                            }
                        )
                        .accessibility(label: Text(LocalizedStringKey("semanticTwitter \(speaker.name)")))
                    }
                    if (speaker.github != nil) {
                        SocialItem(
                            iconName: "ic_github",
                            text: speaker.github!,
                            onClick: {
                                onGitHubClicked(speaker.githubUrl!)
                            }
                        )
                        .accessibility(label: Text(LocalizedStringKey("semanticGitHub \(speaker.name)")))
                    }
                }
                .frame(maxWidth: .infinity, alignment: .leading)
            }
            Text(speaker.bio)
                .font(bodyFont)
                .foregroundColor(color)
        }
        .frame(maxWidth: .infinity)
    }
}

struct SpeakerSectionView_Previews: PreviewProvider {
    static var previews: some View {
        SpeakerSectionView(
            speaker: SpeakerUi.companion.fake,
            onTwitterClicked: { String in },
            onGitHubClicked: { String in }
        )
    }
}
