//
//  Partners.swift
//  iosApp
//
//  Created by GERARD on 22/05/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct Partners<PartnerItem: View>: View {
    let partners: PartnerGroupsUi
    let partnerItem: (PartnerItemUi, CGFloat) -> PartnerItem
    let horizontalSpacing: CGFloat = 16
    
    init(
        partners: PartnerGroupsUi,
        @ViewBuilder partnerItem: @escaping (PartnerItemUi, CGFloat) -> PartnerItem
    ) {
        self.partners = partners
        self.partnerItem = partnerItem
    }

    var body: some View {
        GeometryReader { geometry in
            let parentWidth = geometry.size.width
            ScrollView {
                LazyVStack(spacing: 8) {
                    ForEach(partners.groups, id: \.type) { partnerGroup in
                        Section {
                            PartnerDividerView(text: partnerGroup.type)
                            ForEach(partnerGroup.partners, id: \.[0].id) { partners in
                                PartnerRowView(
                                    partners: partners,
                                    parentWidth: parentWidth - (self.horizontalSpacing * 2),
                                    partnerItem: self.partnerItem
                                )
                            }
                        }
                    }
                }
                .padding(.horizontal, self.horizontalSpacing)
            }
        }
    }
}

struct Partners_Previews: PreviewProvider {
    static var previews: some View {
        Partners(
            partners: PartnerGroupsUi.companion.fake,
            partnerItem: { partner, size in
                Button {
                } label: {
                    RemoteImage(
                        url: partner.logoUrl,
                        description: partner.name,
                        id: partner.id
                    )
                    .padding()
                }
            }
        )
    }
}
