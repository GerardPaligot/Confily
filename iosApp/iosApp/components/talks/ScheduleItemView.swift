//
//  ScheduleItemView.swift
//  iosApp
//
//  Created by GERARD on 06/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct ScheduleItemView<TalkItem: View>: View {
    let time: String
    let talks: [TalkItemUi]
    let talkItem: (TalkItemUi) -> TalkItem

    init(time: String, talks: [TalkItemUi], @ViewBuilder talkItem: @escaping (TalkItemUi) -> TalkItem) {
        self.time = time
        self.talks = talks
        self.talkItem = talkItem
    }
    
    var body: some View {
        VStack(alignment: .leading) {
            Text(time)
                .accessibilityLabel(toSpelloutAccessibleTime(time: time))
                .font(.body)
                .foregroundColor(Color.c4hSecondary)
                .padding(.vertical, 8)
            VStack(spacing: 8.0) {
                ForEach(talks, id: \.id) { talk in
                    self.talkItem(talk)
                }
            }
        }
    }
}

func toSpelloutAccessibleTime(time: String) -> LocalizedStringKey {
    let timeSplitted = time.split(separator: ":")
    let hh = Int(timeSplitted[0]) ?? 0
    let mm = Int(timeSplitted[1]) ?? 0
    return LocalizedStringKey("semanticHHmm \(hh) \(mm)")
}

struct ScheduleItemView_Previews: PreviewProvider {
    static var previews: some View {
        ScheduleItemView(
            time: "10:00",
            talks: [
                TalkItemUi.companion.fake,
                TalkItemUi.companion.fake
            ],
            talkItem: { talk in
                TalkItemView(
                    talk: talk,
                    onFavoriteClicked: { TalkItemUi in }
                )
            }
        )
    }
}
