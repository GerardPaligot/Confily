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
    var speaker: SpeakerUi
    var onTwitterClicked: (_: String) -> ()
    var onGitHubClicked: (_: String) -> ()
    
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
            }
            .padding(.horizontal, 8)
        }
    }
}

struct SpeakerDetail_Previews: PreviewProvider {
    static var previews: some View {
        SpeakerDetail(
            speaker: SpeakerUi.companion.fake,
            onTwitterClicked: { String in },
            onGitHubClicked: { String in }
        )
    }
}
