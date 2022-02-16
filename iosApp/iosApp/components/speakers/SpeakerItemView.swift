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
    var color: Color = Color.c4hOnBackground
    var nameFont: Font = Font.callout.bold()
    var companyFont: Font = Font.caption
    
    var body: some View {
        HStack(alignment: .center, spacing: 16) {
            AsyncImageView(
                url: URL(string: speakerUi.url)!,
                placeholder: {
                    Text("...")
                }
            )
                .frame(width: 48, height: 48)
                .clipShape(Circle())
            VStack(alignment: .leading) {
                Text(speakerUi.name)
                    .foregroundColor(color)
                    .font(nameFont)
                Text(speakerUi.company)
                    .foregroundColor(color.opacity(0.74))
                    .font(companyFont)
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
