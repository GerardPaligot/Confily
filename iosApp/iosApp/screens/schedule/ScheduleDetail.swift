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
    @Environment(\.openURL) var openURL
    @State private var isShareSheetPresented = false
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
                ForEach(talkUi.speakers, id: \.id) { speaker in
                    self.speakerItem(speaker)
                }
                ButtonView(text: NSLocalizedString("actionFeedback", comment: "")) {
                    if let url2 = URL(string: talkUi.openFeedbackUrl!) { openURL(url2) }
                }
            }
            .padding(.horizontal, 16)
        }
        .navigationBarItems(trailing:
            HStack {
                Button(action: {
                    isShareSheetPresented = true
                }, label: {
                    Image(systemName: "square.and.arrow.up")
                })
            }
        )
        .sheet(isPresented: $isShareSheetPresented) {
            let formatter = NSLocalizedString("inputShareTalk", comment: "")
            ShareSheetView(activityItems: [String(format: formatter, talkUi.title, talkUi.speakersSharing)])
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
