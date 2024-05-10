//
//  PauseView.swift
//  iosApp
//
//  Created by GERARD on 15/11/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import SharedDi

struct EventSessionView: View {
    let session: EventSessionItemUi
    let titleColor: Color = Color.c4hOnBackground
    let titleTextStyle: Font = Font.title3

    var body: some View {
        VStack(alignment: .leading) {
            Text(session.title)
                .foregroundColor(titleColor)
                .font(titleTextStyle)
                .frame(maxWidth: .infinity, alignment: .leading)
            HStack {
                TagUnStyledView(
                    text: session.room,
                    icon: "mappin"
                )
                TagUnStyledView(
                    text: "\(session.timeInMinutes) minutes",
                    icon: session.timeInMinutes <= 30 ? "bolt.badge.clock" : "clock"
                )
            }
        }
    }
}

struct EventSessionView_Previews: PreviewProvider {
    static var previews: some View {
        EventSessionView(session: EventSessionItemUi.companion.fakePause)
    }
}
