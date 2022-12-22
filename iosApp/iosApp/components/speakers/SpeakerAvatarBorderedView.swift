//
//  SpeakerAvatarBorderedView.swift
//  iosApp
//
//  Created by GERARD on 22/12/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct SpeakerAvatarBorderedView: View {
    var url: String
    var size: CGFloat = .medium
    var borderSize: CGFloat = 2

    var body: some View {
        SpeakerAvatarView(url: url, size: size)
            .overlay(
                Circle().stroke(Color.c4hOnPrimary, lineWidth: borderSize)
            )
    }
}

struct SpeakerAvatarBorderedView_Previews: PreviewProvider {
    static var previews: some View {
        SpeakerAvatarBorderedView(
            url: SpeakerUi.companion.fake.url
        )
    }
}
