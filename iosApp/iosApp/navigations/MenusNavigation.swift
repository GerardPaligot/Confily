//
//  MenusNavigation.swift
//  iosApp
//
//  Created by GERARD on 03/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct MenusNavigation: View {
    @EnvironmentObject var viewModelFactory: ViewModelFactory

    var body: some View {
        NavigationLink {
            MenusVM(viewModel: self.viewModelFactory.makeMenusViewModel())
        } label: {
            Text("actionMenus")
        }
    }
}
