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

struct RemoteImage: View {
    let url: String
    let description: String?
    let id: String?
    private let isException: Bool
    // Need to be removed after the Devfest Lille event
    private let exceptions: [String: String] = [
        "hw0KkSyFq1FaTh8hz57C": "adeo-leroymerlin",
        "K8aq8RSjQy8vqHAd6uHZ": "azfalte",
        "PHwo6wyY149snPikjCxA": "cgi",
        "C6cfJWrlr1XZHit4eMPK": "elosi"
    ]
    
    init(url: String, description: String?, id: String? = nil) {
        self.url = url
        self.description = description
        self.id = id
        self.isException = id != nil && exceptions.keys.contains(id!)
    }

    var body: some View {
        Group {
            if (self.isException) {
                Image(exceptions[id!]!)
                    .resizable()
            } else {
                WebImage(url: URL(string: url)!)
                    .placeholder(content: {
                        Text("...")
                    })
                    .resizable()
            }
        }
        .scaledToFit()
        .if(description != nil) { view in
            view.accessibilityLabel(description!)
        }
    }
}

struct PartnerItemView_Previews: PreviewProvider {
    static var previews: some View {
        RemoteImage(
            url: PartnerItemUi.companion.fake.logoUrl,
            description: PartnerItemUi.companion.fake.name,
            id: PartnerItemUi.companion.fake.id
        )
            .frame(width: 250, height: 250)
    }
}
