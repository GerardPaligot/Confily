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
    @EnvironmentObject var viewModelFactory: ViewModelFactory
    let partners: Array<PartnerItemUi>
    let parentWidth: CGFloat
    let maxItems: Int = 3

    var body: some View {
        HStack(spacing: 8) {
            let maxItemsFloat = CGFloat(maxItems)
            let width = (parentWidth - (8 * (maxItemsFloat - 1))) / maxItemsFloat
            ForEach(partners, id: \.id) { partner in
                NavigationLink {
                    PartnerDetailVM(
                        viewModel: viewModelFactory.makePartnerDetailViewModel(partnerId: partner.id)
                    )
                } label: {
                    RemoteImage(
                        url: partner.logoUrl,
                        description: partner.name,
                        id: partner.id
                    )
                    .padding()
                    .frame(width: width, height: width)
                    .background(Color.white)
                    .clipShape(RoundedRectangle(cornerRadius: 8))
                }
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
