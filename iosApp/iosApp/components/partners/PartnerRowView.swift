//
//  PartnerRowView.swift
//  iosApp
//
//  Created by GERARD on 03/05/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct PartnerRowView: View {
    var partners: Array<PartnerItemUi>
    var parentWidth: CGFloat
    var onPartnerClicked: (_: String) -> ()
    var maxItems: Int = 3

    var body: some View {
        HStack(spacing: 8) {
            let maxItemsFloat = CGFloat(maxItems)
            let width = (parentWidth - (8 * (maxItemsFloat - 1))) / maxItemsFloat
            ForEach(partners, id: \.id) { partner in
                Button {
                    onPartnerClicked(partner.siteUrl!)
                } label: {
                    PartnerItemView(
                        id: partner.id,
                        name: partner.name,
                        logoUrl: partner.logoUrl,
                        siteUrl: partner.siteUrl!
                    )
                }
                .frame(width: width, height: width)
                .background(Color.white)
                .clipShape(RoundedRectangle(cornerRadius: 8))
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
                onPartnerClicked: { String in }
            )
        }
    }
}
