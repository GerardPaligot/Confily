//
//  EventItemView.swift
//  iosApp
//
//  Created by GERARD on 16/01/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct EventItemView: View {
    var item: EventItemUi
    
    var body: some View {
        HStack {
            Text(item.name)
                .font(.subheadline)
            Spacer()
            Text(item.date)
                .font(.caption2)
        }
        .padding([.vertical], 16)
    }
}

struct EventItemView_Previews: PreviewProvider {
    static var previews: some View {
        EventItemView(
            item: EventItemUi.companion.fake
        )
    }
}
