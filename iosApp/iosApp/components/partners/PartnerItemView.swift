//
//  PartnerItemView.swift
//  iosApp
//
//  Created by GERARD on 03/05/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared
import SDWebImageSwiftUI

struct PartnerItemView: View {
    var name: String
    var logoUrl: String
    var siteUrl: String

    var body: some View {
        AnimatedImage(
            url: URL(string: logoUrl)!
        )
        .resizable()
        .scaledToFit()
        .padding()
        .accessibilityLabel(name)
    }
}

struct PartnerItemView_Previews: PreviewProvider {
    static var previews: some View {
        PartnerItemView(
            name: PartnerItemUi.companion.fake.name,
            logoUrl: PartnerItemUi.companion.fake.logoUrl,
            siteUrl: PartnerItemUi.companion.fake.siteUrl!
        )
            .frame(width: 250, height: 250)
    }
}
