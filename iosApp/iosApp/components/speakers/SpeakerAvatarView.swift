//
//  SpeakerAvatarView.swift
//  iosApp
//
//  Created by GERARD on 17/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

extension CGFloat {
    public static let small: CGFloat = 30
    public static let medium: CGFloat = 48
    public static let large: CGFloat = 96
}

struct SpeakerAvatarView: View {
    var url: String
    var size: CGFloat = .medium

    var body: some View {
        RemoteImage(url: url, description: nil)
            .frame(width: size, height: size)
            .clipShape(Circle())
            .accessibility(hidden: true)
    }
}

struct SpeakerAvatarView_Previews: PreviewProvider {
    static var previews: some View {
        SpeakerAvatarView(
            url: SpeakerUi.companion.fake.url
        )
    }
}
