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
            VStack(alignment: .leading) {
                Text(talkUi.title)
                    .font(titleFont)
                    .padding(.top, 8)
                Text("textScheduleTime \(talkUi.startTime) \(talkUi.endTime)")
                    .padding(.top, 8)
                    .accessibility(label: Text(toSpelloutAccessibleTime(startTime: talkUi.startTime, endTime: talkUi.endTime)))
                Text(talkUi.room)
                if (talkUi.level != nil) {
                    switch talkUi.level {
                        case "advanced": Text("textLevelAdvanced")
                        case "intermediate": Text("textLevelIntermediate")
                        case "beginner": Text("textLevelBeginner")
                        default: Text(talkUi.level!)
                    }
                }
            }
            .accessibilityElement(children: .combine)
            Text(talkUi.abstract)
                .foregroundColor(color.opacity(0.74))
                .padding(.top, 8)
        }
        .frame(maxWidth: .infinity, alignment: .topLeading)
        .foregroundColor(color)
        .font(bodyFont)
    }
}

func toSpelloutAccessibleTime(startTime: String, endTime: String) -> String {
    let startSplitted = startTime.split(separator: ":")
    let endSplitted = endTime.split(separator: ":")
    return NSLocalizedString("semanticScheduleTime \(startSplitted[0]) \(startSplitted[1]) \(endSplitted[0]) \(endSplitted[1])", comment: "")
}

struct TalkSectionView_Previews: PreviewProvider {
    static var previews: some View {
        TalkSectionView(
            talkUi: TalkUi.companion.fake
        )
    }
}
