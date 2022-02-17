//
//  SpeakerAvatarView.swift
//  iosApp
//
//  Created by GERARD on 17/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct SpeakerAvatarView: View {
    var url: String
    
    var body: some View {
        AsyncImageView(
            url: URL(string: url)!,
            placeholder: {
                Text("...")
            }
        )
            .clipShape(Circle())
    }
}

struct SpeakerAvatarView_Previews: PreviewProvider {
    static var previews: some View {
        SpeakerAvatarView(
            url: SpeakerUi.companion.fake.url
        )
    }
}
