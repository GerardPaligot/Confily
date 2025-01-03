//
//  TalkSectionView.swift
//  iosApp
//
//  Created by GERARD on 15/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import SharedDi

struct TalkSectionView: View {
    var infoUi: SessionInfoUi
    var abstract: String
    var color: Color = Color.c4hOnBackground
    var titleFont: Font = Font.title
    var bodyFont: Font = Font.callout

    var body: some View {
        VStack(alignment: .leading) {
            if (infoUi.category.color != nil || infoUi.level != nil) {
                HStack(spacing: 8) {
                    if (infoUi.category.color != nil) {
                        DecorativeTagView(category: infoUi.category)
                    }
                    if (infoUi.level != nil) {
                        LevelTagView(level: infoUi.level!)
                    }
                }
            }
            VStack(alignment: .leading, spacing: 24) {
                Text(infoUi.title)
                    .font(titleFont)
                    .padding(.top, 8)
                HStack {
                    TagUnStyledView(
                        text: infoUi.slotTime,
                        icon: "deskclock"
                    )
                    TagUnStyledView(
                        text: infoUi.room,
                        icon: "video"
                    )
                    TagUnStyledView(
                        text: "\(infoUi.timeInMinutes) minutes",
                        icon: infoUi.timeInMinutes <= 30 ? "bolt.badge.clock" : "clock"
                    )
                }
            }
            .accessibilityElement(children: .combine)
            Text(.init(abstract))
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
            infoUi: SessionInfoUi.companion.fake,
            abstract: SessionUi.companion.fake.abstract
        )
    }
}
