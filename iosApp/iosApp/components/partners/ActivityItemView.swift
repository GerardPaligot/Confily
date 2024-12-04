//
//  ActivityItemView.swift
//  iosApp
//
//  Created by Gérard PALIGOT on 04/12/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import SharedDi

struct ActivityItemView: View {
    var activity: ActivityUi
    var titleColor: Color = Color.c4hOnBackground
    var titleTextStyle: Font = Font.title3
    
    var body: some View {
        VStack(alignment: .leading) {
            Text(activity.activityName)
                .foregroundColor(titleColor)
                .font(titleTextStyle)
            HStack {
                TagUnStyledView(
                    text: activity.startTime,
                    icon: "clock"
                )
                TagUnStyledView(
                    text: activity.partnerName,
                    icon: "suitcase"
                )
            }
        }
        .frame(maxWidth: .infinity, alignment: .topLeading)
    }
}

#Preview {
    ActivityItemView(
        activity: ActivityUi.companion.fake
    )
    .padding()
}
