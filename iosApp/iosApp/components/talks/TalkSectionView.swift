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
    var titleFont: Font = Font.title
    var bodyFont: Font = Font.callout

    var body: some View {
        VStack(alignment: .leading) {
            if (talkUi.category.color != nil || talkUi.level != nil) {
                HStack(spacing: 8) {
                    if (talkUi.category.color != nil) {
                        DecorativeTagView(category: talkUi.category)
                    }
                    if (talkUi.level != nil) {
                        LevelTagView(level: talkUi.level!)
                    }
                }
            }
            VStack(alignment: .leading, spacing: 24) {
                Text(talkUi.title)
                    .font(titleFont)
                    .padding(.top, 8)
                HStack {
                    TagUnStyledView(
                        text: talkUi.startTime,
                        icon: "deskclock"
                    )
                    TagUnStyledView(
                        text: talkUi.room,
                        icon: "video"
                    )
                    TagUnStyledView(
                        text: "\(talkUi.timeInMinutes) minutes",
                        icon: talkUi.timeInMinutes <= 30 ? "bolt.badge.clock" : "clock"
                    )
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

func toSpelloutAccessibleTime(startTime: String, endTime: String) -> LocalizedStringKey {
    let startSplitted = startTime.split(separator: ":")
    let startHH = Int(startSplitted[0]) ?? 0
    let startMM = Int(startSplitted[1]) ?? 0
    let endSplitted = endTime.split(separator: ":")
    let endHH = Int(endSplitted[0]) ?? 0
    let endMM = Int(endSplitted[1]) ?? 0
    return LocalizedStringKey("semanticScheduleTime \(startHH) \(startMM) \(endHH) \(endMM)")
}

struct TalkSectionView_Previews: PreviewProvider {
    static var previews: some View {
        TalkSectionView(
            talkUi: TalkUi.companion.fake
        )
    }
}
