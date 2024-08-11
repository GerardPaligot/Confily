//
//  PartnerItemView.swift
//  iosApp
//
//  Created by GERARD on 03/05/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import SharedDi
import SDWebImageSwiftUI

struct RemoteImage: View {
    let url: String
    let description: String?
    let id: String?
    
    init(url: String, description: String?, id: String? = nil) {
        self.url = url
        self.description = description
        self.id = id
    }

    var body: some View {
        WebImage(url: URL(string: url)!) { image in
            image.resizable()
                .scaledToFit()
                .if(description != nil) { view in
                    view.accessibilityLabel(description!)
                }
        } placeholder: {
            Text("...")
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
