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
                Toggle(isOn: Binding(
                    get: { filtersUi.onlyFavorites },
                    set: { onFavoriteSelected($0) }
                ), label: {
                    Text("actionFilteringFavorites")
                })
            }
            Section(header: Text("titleFiltersCategories")) {
                ForEach(Array(filtersUi.categories.keys), id: \.self) { key in
                    let isOn = filtersUi.categories[key] as? Bool ?? false
                    Toggle(isOn: Binding(
                        get: { isOn },
                        set: { onCategorySelected(key, $0) }
                    ), label: {
                        Text(key.name)
                    })
                }
            }
            Section(header: Text("titleFiltersFormats")) {
                ForEach(Array(filtersUi.formats.keys), id: \.self) { key in
                    let isOn = filtersUi.formats[key] as? Bool ?? false
                    Toggle(isOn: Binding(
                        get: { isOn },
                        set: { onFormatSelected(key, $0) }
                    ), label: {
                        Text(key.name)
                    })
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
