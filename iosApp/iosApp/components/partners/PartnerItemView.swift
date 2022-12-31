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
    var id: String
    var name: String
    var logoUrl: String
    // Need to be removed after the Devfest Lille event
    let exceptions: [String: String] = [
        "hw0KkSyFq1FaTh8hz57C": "adeo-leroymerlin",
        "K8aq8RSjQy8vqHAd6uHZ": "azfalte",
        "PHwo6wyY149snPikjCxA": "cgi",
        "C6cfJWrlr1XZHit4eMPK": "elosi"
    ]

    var body: some View {
        if (exceptions.keys.contains(id)) {
            Image(exceptions[id]!)
                .resizable()
                .scaledToFit()
                .padding()
                .accessibilityLabel(name)
        } else {
            WebImage(url: URL(string: logoUrl)!)
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
            id: PartnerItemUi.companion.fake.id,
            name: PartnerItemUi.companion.fake.name,
            logoUrl: PartnerItemUi.companion.fake.logoUrl
        )
            .frame(width: 250, height: 250)
    }
}
