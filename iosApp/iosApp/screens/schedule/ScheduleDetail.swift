//
//  ScheduleDetail.swift
//  iosApp
//
//  Created by GERARD on 15/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import SharedDi

struct ScheduleDetail: View {
    @State private var isShareSheetPresented = false
    let sessionUi: SessionUi
    
    var body: some View {
        ScrollView {
            LazyVStack(spacing: 8) {
                TalkSectionView(
                    infoUi: sessionUi.info,
                    abstract: sessionUi.abstract
                )
                .padding(.bottom, 8)
                ForEach(sessionUi.speakers, id: \.id) { speaker in
                    SpeakerItemNavigation(speaker: speaker)
                }
                if (sessionUi.canGiveFeedback) {
                    Link("actionFeedback", destination: URL(string: sessionUi.openFeedbackUrl!)!)
                } else {
                    Text("textFeedbackNotStarted")
                        .font(.caption)
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
            ShareSheetView(activityItems: [String(format: formatter, sessionUi.info.title, sessionUi.speakersSharing)])
        }
    }
}

struct ScheduleDetail_Previews: PreviewProvider {
    static var previews: some View {
        ScheduleDetail(sessionUi: SessionUi.companion.fake)
            .environmentObject(ViewModelFactory())
    }
}
