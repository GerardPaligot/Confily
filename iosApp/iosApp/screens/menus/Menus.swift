//
//  Menus.swift
//  iosApp
//
//  Created by GERARD on 22/05/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct Menus: View {
    var menuItems: Array<MenuItemUi>
    
    var body: some View {
        ScrollView {
            LazyVStack(spacing: 16) {
                ForEach(menuItems, id: \.self) { menuItem in
                    MenuItemView(menuItem: menuItem)
                        .padding()
                        .frame(maxWidth: .infinity, alignment: .leading)
                }
            }
            .padding(.horizontal, 8)
        }
    }
}

struct Menus_Previews: PreviewProvider {
    static var previews: some View {
        Menus(
            menuItems: [MenuItemUi.companion.fake, MenuItemUi.companion.fake]
        )
    }
}
