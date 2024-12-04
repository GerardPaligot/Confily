//
//  Partners.swift
//  iosApp
//
//  Created by GERARD on 22/05/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import SharedDi

struct Partners: View {
    var uiModel: PartnersActivitiesUi
    @State private var selectedTab = "screenPartners"

    var body: some View {
        VStack {
            if (self.uiModel.activities.keys.count > 0) {
                Picker("Options:", selection: $selectedTab) {
                    Text("screenPartners").tag("screenPartners")
                    Text("screenActivities").tag("screenActivities")
                }
                .pickerStyle(SegmentedPickerStyle())
            }
            if (selectedTab == "screenActivities") {
                PartnersActivitiesView(activities: uiModel.activities)
            } else {
                PartnerListView(partners: uiModel.partners)
            }
        }
        .navigationTitle(Text("screenPartners"))
        .navigationBarTitleDisplayMode(.inline)
    }
}

#Preview {
    Partners(uiModel: PartnersActivitiesUi.companion.fake)
}
