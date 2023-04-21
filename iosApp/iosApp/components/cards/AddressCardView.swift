//
//  AddressCardView.swift
//  iosApp
//
//  Created by GERARD on 30/12/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct AddressCardView: View {
    var formattedAddress: Array<String>
    
    var body: some View {
        HStack(alignment: .center) {
            VStack(alignment: .leading) {
                ForEach(formattedAddress.indices, id: \.self) { index in
                    let line = formattedAddress[index]
                    if (index == 0) {
                        Text(line)
                            .font(.callout)
                            .fontWeight(.bold)
                    } else {
                        Text(line)
                            .font(.callout)
                    }
                }
            }
        }
        .frame(maxWidth: .infinity, alignment: .topLeading)
        .accessibilityElement(children: .combine)
    }
}

struct AddressCardView_Previews: PreviewProvider {
    static var previews: some View {
        AddressCardView(
            formattedAddress: EventUi.companion.fake.eventInfo.formattedAddress
        )
    }
}
