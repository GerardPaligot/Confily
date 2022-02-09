//
//  ScheduleItemView.swift
//  iosApp
//
//  Created by GERARD on 06/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct ScheduleItemView: View {
    var time: String
    var talks: [TalkItemUi]
    var onTalkClicked: (_: String) -> ()
    var onFavoriteClicked: (_: String, _: Bool) -> ()
    
    var body: some View {
        let timeSpace = 55.0
        ZStack(alignment: .topLeading) {
            Text(time)
                .font(.body)
                .foregroundColor(Color.c4hSecondary)
                .frame(width: timeSpace, alignment: .center)
            VStack(spacing: 4.0) {
                ForEach(talks, id: \.id) { talk in
                    ZStack {
                        TalkItemView(
                            talk: talk,
                            onFavoriteClicked: onFavoriteClicked)
                            .padding(.leading, timeSpace)
                    }
                    .onTapGesture { onTalkClicked(talk.id) }
                }
            }
        }
    }
}

struct ScheduleItemView_Previews: PreviewProvider {
    static var previews: some View {
        ScheduleItemView(
            time: "10:00",
            talks: [
                TalkItemUi.companion.fake,
                TalkItemUi.companion.fake
            ],
            onTalkClicked: { String in },
            onFavoriteClicked: { String, Bool in }
        )
    }
}
