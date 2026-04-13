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
    @State private var showFilters = false
    
    var body: some View {
        Button {
            showFilters = true
        } label: {
            Image(systemName: "line.3.horizontal.decrease.circle")
                .accessibilityLabel("actionFilteringFavorites")
        }
        .buttonStyle(.plain)
        .sheet(isPresented: $showFilters) {
            AgendaFiltersVM(
                viewModel: viewModelFactory.makeAgendaFiltersViewModel()
            )
        }
    }
}
