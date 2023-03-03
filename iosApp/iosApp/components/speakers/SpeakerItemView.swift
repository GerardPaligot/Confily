//
//  SpeakerItemView.swift
//  iosApp
//
//  Created by GERARD on 15/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct SpeakerItemView: View {
    var speakerUi: SpeakerItemUi
    
    var body: some View {
        HStack(alignment: .center, spacing: 16) {
            SpeakerAvatarView(url: speakerUi.url, size: .medium)
            VStack(alignment: .leading) {
                Text(speakerUi.name)
                    .font(Font.callout.bold())
                Text(speakerUi.company)
                    .foregroundColor(.secondary)
                    .font(Font.caption)
            }
        }
        .frame(maxWidth: .infinity, alignment: .topLeading)
    }
}

struct SpeakerItemView_Previews: PreviewProvider {
    static var previews: some View {
        SpeakerItemView(
            speakerUi: SpeakerItemUi.companion.fake
        )
    }
}
