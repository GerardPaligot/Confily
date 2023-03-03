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
    let partners: Array<PartnerItemUi>
    let parentWidth: CGFloat
    let maxItems: Int = 3

    var body: some View {
        HStack(spacing: 8) {
            let maxItemsFloat = CGFloat(maxItems)
            let width = (parentWidth - (8 * (maxItemsFloat - 1))) / maxItemsFloat
            ForEach(partners, id: \.id) { partner in
                PartnerItemNavigation(
                    partner: partner,
                    size: width
                )
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
                parentWidth: parentWidth
            )
        }
        .environmentObject(ViewModelFactory())
    }
}
