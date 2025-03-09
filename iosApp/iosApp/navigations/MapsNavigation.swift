//
//  MapsNavigation.swift
//  iosApp
//
//  Created by Gérard PALIGOT on 09/03/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI

struct MapsNavigation: View {
    @EnvironmentObject var viewModelFactory: ViewModelFactory

    var body: some View {
        NavigationLink {
            MapVM(viewModel: self.viewModelFactory.makeMapsViewModel())
        } label: {
            Text("actionsMaps")
        }
    }
}
