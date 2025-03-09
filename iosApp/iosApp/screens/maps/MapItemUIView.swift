//
//  MapItemUIView.swift
//  iosApp
//
//  Created by Gérard PALIGOT on 09/03/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI
import SharedDi

struct MapItemUIView: View {
    let maps: Array<MapFilledEntity>
    
    var body: some View {
        List {
            ForEach(maps, id: \.url) { map in
                Section(header: Text(map.name)) {
                    RemoteImage(
                        url: map.url,
                        description: nil,
                        id: map.url
                    )
                    .padding()
                    .frame(width: .infinity)
                }
            }
        }
        .listStyle(.grouped)
    }
}
