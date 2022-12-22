//
//  SpeakerHeaderView.swift
//  iosApp
//
//  Created by GERARD on 16/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct SpeakerHeaderView: View {
    var url: String
    var name: String
    var company: String
    var color: Color = Color.c4hOnBackground
    var titleFont: Font = Font.headline
    var companyFont: Font = Font.subheadline
    
    var body: some View {
        VStack {
            SpeakerAvatarView(
                url: url,
                size: .large
            )
            Text(name)
                .font(titleFont)
                .padding(.top, 24)
            Text(company)
                .font(companyFont)
        }
        .foregroundColor(color)
        .frame(maxWidth: .infinity)
    }
}

struct SpeakerHeaderView_Previews: PreviewProvider {
    static var previews: some View {
        let speaker = SpeakerUi.companion.fake
        SpeakerHeaderView(
            url: speaker.url,
            name: speaker.name,
            company: speaker.company
        )
    }
}
