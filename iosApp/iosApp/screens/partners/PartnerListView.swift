//
//  PartnerListView.swift
//  iosApp
//
//  Created by Gérard PALIGOT on 04/12/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import SharedDi

struct PartnerListView: View {
    let partners: PartnerGroupsUi
    let horizontalSpacing: CGFloat = 16
    let maxItems: Int = 3

    var body: some View {
        GeometryReader { geometry in
            let parentWidth = geometry.size.width  - (self.horizontalSpacing * 2)
            ScrollView {
                LazyVStack(spacing: 8) {
                    ForEach(partners.groups, id: \.type) { partnerGroup in
                        Section {
                            LazyVGrid(columns: [GridItem(.adaptive(minimum: parentWidth)), GridItem(.adaptive(minimum: parentWidth)), GridItem(.adaptive(minimum: parentWidth))], content: {
                                ForEach(partnerGroup.partners, id: \.id) { partner in
                                    PartnerItemNavigation(partner: partner)
                                }
                            })
                        } header : {
                            PartnerDividerView(text: partnerGroup.type)
                                .padding(.horizontal, self.horizontalSpacing)
                        }
                    }
                }
            }
        }
    }
}

#Preview {
    PartnerListView(partners: PartnerGroupsUi.companion.fake)
        .environmentObject(ViewModelFactory())
}
