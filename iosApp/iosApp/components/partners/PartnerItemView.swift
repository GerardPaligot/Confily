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
    // Need to be removed after the Devfest Lille event
    let exceptions: [String: String] = [
        "Leroy Merlin - Adeo": "adeo-leroymerlin",
        "Azfalte": "azfalte",
        "CGI": "cgi",
        "ELOSI": "elosi"
    ]

    var body: some View {
        if (exceptions.keys.contains(name)) {
            Image(exceptions[name]!)
                .resizable()
                .scaledToFit()
                .padding()
                .accessibilityLabel(name)
        } else {
            AnimatedImage(url: URL(string: logoUrl)!)
                .resizable()
                .scaledToFit()
                .padding()
                .accessibilityLabel(name)
        }
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
