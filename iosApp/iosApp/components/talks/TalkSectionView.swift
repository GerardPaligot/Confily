//
//  TalkSectionView.swift
//  iosApp
//
//  Created by GERARD on 15/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct TalkSectionView: View {
    var talkUi: TalkUi
    var color: Color = Color.c4hOnBackground
    var titleFont: Font = Font.headline
    var bodyFont: Font = Font.callout

    var body: some View {
        VStack(alignment: .leading) {
            Text(talkUi.title)
                .font(titleFont)
                .padding(.top, 8)
            Text(talkUi.date)
                .padding(.top, 8)
            Text(talkUi.room)
            if (talkUi.level != nil) {
                Text(talkUi.level!)
            }
            Text(talkUi.abstract)
                .foregroundColor(color.opacity(0.74))
                .padding(.top, 8)
        }
        .frame(maxWidth: .infinity, alignment: .topLeading)
        .foregroundColor(color)
        .font(bodyFont)
    }
}

struct TalkSectionView_Previews: PreviewProvider {
    static var previews: some View {
        TalkSectionView(
            talkUi: TalkUi.companion.fake
        )
    }
}
