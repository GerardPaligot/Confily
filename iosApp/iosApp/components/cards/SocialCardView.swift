//
//  SocialCardView.swift
//  iosApp
//
//  Created by GERARD on 31/12/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct SocialCardView: View {
    let title: String
    let message: String
    let icon: String

    var body: some View {
        VStack(alignment: .leading,spacing: 8) {
            HStack(alignment: .bottom,spacing: 8) {
                Image(icon)
                    .renderingMode(.template)
                    .resizable()
                    .frame(width: 24, height: 24)
                Text(title).fontWeight(.bold)
            }
            Text(message).font(.callout)
        }
        .frame(maxWidth: .infinity, alignment: .topLeading)
        .padding()
        .background(Color.c4hSurface)
        .foregroundColor(Color.c4hOnSurface)
        .modifier(CardModifier(elevation: 2))
    }
}

struct SocialCardView_Previews: PreviewProvider {
    static var previews: some View {
        SocialCardView(
            title: "Twitter message",
            message: PartnerItemUi.companion.fake.twitterMessage!,
            icon: "ic_twitter"
        )
    }
}
