//
//  ScheduleDetail.swift
//  iosApp
//
//  Created by GERARD on 15/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct ScheduleDetail: View {
    var talkUi: TalkUi
    var onSpeakerClicked: (_: String) -> ()
    
    var body: some View {
        ScrollView {
            LazyVStack(spacing: 8) {
                TalkSectionView(talkUi: talkUi)
                    .padding(.bottom, 8)
                    .padding(.horizontal, 8)
                ForEach(talkUi.speakers, id: \.id) { speaker in
                    Button {
                        onSpeakerClicked(speaker.id)
                    } label: {
                        SpeakerItemView(speakerUi: speaker)
                            .padding(.horizontal, 8)
                    }
                }
            }
        }
    }
}

struct ScheduleDetail_Previews: PreviewProvider {
    static var previews: some View {
        ScheduleDetail(
            talkUi: TalkUi.companion.fake,
            onSpeakerClicked: { String in }
        )
    }
}
