//
//  SpeakersAvatarView.swift
//  iosApp
//
//  Created by GERARD on 22/12/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct SpeakersAvatarView: View {
    var speakers: Array<String>
    var avatars: Array<String>
    
    var body: some View {
        ZStack(alignment: .trailing) {
            ForEach(avatars.indices, id: \.self) { index in
                SpeakerAvatarBorderedView(
                    url: avatars[index],
                    size: .small
                )
                .padding([.trailing], (CGFloat(index) * 20))
                .accessibilityLabel(speakers[index])
            }
        }
    }
}

struct SpeakersAvatarView_Previews: PreviewProvider {
    static var previews: some View {
        SpeakersAvatarView(
            speakers: TalkItemUi.companion.fake.speakers,
            avatars: TalkItemUi.companion.fake.speakersAvatar
        )
    }
}
