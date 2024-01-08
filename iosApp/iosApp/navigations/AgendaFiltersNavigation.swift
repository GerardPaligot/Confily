//
//  AgendaFiltersNavigation.swift
//  iosApp
//
//  Created by GERARD on 08/01/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct AgendaFiltersNavigation: View {
    @EnvironmentObject var viewModelFactory: ViewModelFactory
    
    var body: some View {
        NavigationLink {
            AgendaFiltersVM(
                viewModel: viewModelFactory.makeAgendaFiltersViewModel()
            )
        } label: {
            Image(systemName: "line.3.horizontal.decrease.circle")
                .accessibilityLabel("actionFilteringFavorites")
        }
        .buttonStyle(.plain)
    }
}
