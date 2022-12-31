//
//  PartnerRowView.swift
//  iosApp
//
//  Created by GERARD on 03/05/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct PartnerRowView<PartnerItem: View>: View {
    let partners: Array<PartnerItemUi>
    let parentWidth: CGFloat
    let partnerItem: (PartnerItemUi, CGFloat) -> PartnerItem
    let maxItems: Int = 3
    
    init(
        partners: Array<PartnerItemUi>,
        parentWidth: CGFloat,
        @ViewBuilder partnerItem: @escaping (PartnerItemUi, CGFloat) -> PartnerItem
    ) {
        self.partners = partners
        self.parentWidth = parentWidth
        self.partnerItem = partnerItem
    }

    var body: some View {
        HStack(spacing: 8) {
            let maxItemsFloat = CGFloat(maxItems)
            let width = (parentWidth - (8 * (maxItemsFloat - 1))) / maxItemsFloat
            ForEach(partners, id: \.id) { partner in
                self.partnerItem(partner, width)
            }
        }
    }
}

struct PartnerRowView_Previews: PreviewProvider {
    static var previews: some View {
        GeometryReader { geometry in
            let parentWidth = geometry.size.width
            PartnerRowView(
                partners: [
                    PartnerItemUi.companion.fake,
                    PartnerItemUi.companion.fake,
                    PartnerItemUi.companion.fake
                ],
                parentWidth: parentWidth,
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
}
