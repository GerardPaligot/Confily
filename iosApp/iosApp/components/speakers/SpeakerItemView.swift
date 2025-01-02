//
//  SpeakerItemView.swift
//  iosApp
//
//  Created by GERARD on 15/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import SharedDi

struct SpeakerItemView: View {
    var url: String?
    var title: String
    var description: String
    
    var body: some View {
        HStack(alignment: .center, spacing: 16) {
            if (url != nil) {
                SpeakerAvatarView(url: url!, size: .medium)
            }
            VStack(alignment: .leading) {
                Text(title)
                    .font(Font.callout.bold())
                Text(description)
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
            url: SpeakerItemUi.companion.fake.url,
            title: SpeakerItemUi.companion.fake.name,
            description: SpeakerItemUi.companion.fake.activity
        )
    }
}
