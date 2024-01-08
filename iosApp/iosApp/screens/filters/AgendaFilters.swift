//
//  AgendaFilters.swift
//  iosApp
//
//  Created by GERARD on 08/01/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import SharedDi

struct AgendaFilters: View {
    let filtersUi: FiltersUi
    var onFavoriteSelected: (Bool) -> ()
    var onCategorySelected: (CategoryUi, Bool) -> ()
    var onFormatSelected: (FormatUi, Bool) -> ()
    
    var body: some View {
        List {
            Section(header: Text("titleFiltersFavorites")) {
                Toggle(isOn: Binding.constant(filtersUi.onlyFavorites), label: {
                    Text("actionFilteringFavorites")
                })
                .onTapGesture {
                    onFavoriteSelected(!filtersUi.onlyFavorites)
                }
            }
            Section(header: Text("titleFiltersCategories")) {
                ForEach(Array(filtersUi.categories.keys), id: \.self) { key in
                    let isOn = filtersUi.categories[key] ?? false
                    Toggle(isOn: Binding.constant(isOn as! Bool), label: {
                        Text(key.name)
                    })
                    .onTapGesture {
                        onCategorySelected(key, !(isOn as! Bool))
                    }
                }
            }
            Section(header: Text("titleFiltersFormats")) {
                ForEach(Array(filtersUi.formats.keys), id: \.self) { key in
                    let isOn = filtersUi.formats[key] ?? false
                    Toggle(isOn: Binding.constant(isOn as! Bool), label: {
                        Text(key.name)
                    })
                    .onTapGesture {
                        onFormatSelected(key, !(isOn as! Bool))
                    }
                }
            }
        }
        .navigationTitle(Text("screenFilters"))
        .navigationBarTitleDisplayMode(.inline)
    }
}

#Preview {
    AgendaFilters(
        filtersUi: FiltersUi.companion.fake,
        onFavoriteSelected: { selected in },
        onCategorySelected: { category, selected in },
        onFormatSelected: { format, selected in }
    )
}
