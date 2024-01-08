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
        .navigationTitle(Text("screenPartners"))
        .navigationBarTitleDisplayMode(.inline)
    }
}

struct Partners_Previews: PreviewProvider {
    static var previews: some View {
        NavigationView {
            Partners(partners: PartnerGroupsUi.companion.fake)
                .environmentObject(ViewModelFactory())
        }
    }
}
