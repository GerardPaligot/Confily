//
//  PartnersActivitiesView.swift
//  iosApp
//
//  Created by Gérard PALIGOT on 04/12/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import SharedDi

struct PartnersActivitiesView: View {
    var activities: [String: [ActivityUi]]
    
    var body: some View {
        List {
            ForEach(activities.keys.sorted(), id: \.self) { time in
                Section {
                    let activities = activities[time]!
                    ForEach(0..<activities.count, id: \.self) { index in
                        let activity = activities[index]
                        ActivityItemView(activity: activity)
                    }
                } header: {
                    Text(time)
                        .accessibilityLabel(toSpelloutAccessibleTime(time: time))
                }
            }
        }
    }
}

#Preview {
    PartnersActivitiesView(
        activities: [
            "09:00": [
                ActivityUi.companion.fake
            ],
            "10:00": [
                ActivityUi.companion.fake
            ]
        ]
    )
}
