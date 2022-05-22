//
//  MenuItemView.swift
//  iosApp
//
//  Created by GERARD on 22/05/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared
import MapKit

struct MenuItemView: View {
    var menuItem: MenuItemUi

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text(menuItem.name)
                .font(.title3)
                .fontWeight(.bold)
            VStack(alignment: .leading, spacing: 4) {
                Text(menuItem.dish)
                Text(menuItem.accompaniment)
                Text(menuItem.dessert)
            }
            .frame(width: .infinity)
            .accessibilityElement(children: .combine)
        }
        .frame(width: .infinity)
    }
}

struct MenuItemView_Previews: PreviewProvider {
    static var previews: some View {
        MenuItemView(
            menuItem: MenuItemUi.companion.fake
        )
    }
}
