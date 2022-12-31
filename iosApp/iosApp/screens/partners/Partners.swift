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
                                    parentWidth: parentWidth,
                                    partnerItem: self.partnerItem
                                )
                            }
                        }
                    }
                }
                .padding(.horizontal, 4)
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
                    PartnerItemView(
                        id: partner.id,
                        name: partner.name,
                        logoUrl: partner.logoUrl
                    )
                }
            }
        )
    }
}
