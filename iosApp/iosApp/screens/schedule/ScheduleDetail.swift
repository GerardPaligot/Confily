//
//  ScheduleDetail.swift
//  iosApp
//
//  Created by GERARD on 15/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct ScheduleDetail<SpeakerItem: View>: View {
    let talkUi: TalkUi
    let speakerItem: (SpeakerItemUi) -> SpeakerItem
    
    init(talkUi: TalkUi, @ViewBuilder speakerItem: @escaping (SpeakerItemUi) -> SpeakerItem) {
        self.talkUi = talkUi
        self.speakerItem = speakerItem
    }
    
    var body: some View {
        ScrollView {
            LazyVStack(spacing: 8) {
                TalkSectionView(talkUi: talkUi)
                    .padding(.bottom, 8)
                    .padding(.horizontal, 8)
                ForEach(talkUi.speakers, id: \.id) { speaker in
                    self.speakerItem(speaker)
                        .padding(.horizontal, 8)
                }
            }
        }
    }
}

struct ScheduleDetail_Previews: PreviewProvider {
    static var previews: some View {
        ScheduleDetail(
            talkUi: TalkUi.companion.fake,
            speakerItem: { speaker in
                SpeakerItemView(speakerUi: speaker)
            }
        )
    }
}
