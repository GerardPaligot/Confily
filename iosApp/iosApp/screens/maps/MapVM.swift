//
//  TeamMemberVM.swift
//  iosApp
//
//  Created by Gérard PALIGOT on 02/01/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI

struct MapVM: View {
    @ObservedObject var viewModel: MapViewModel

    var body: some View {
        let uiState = viewModel.uiState
        Group {
            switch uiState {
                case .success(let maps):
                    MapItemUIView(maps: maps)
                case .failure:
                    Text("textError")
                case .loading:
                    Text("textLoading")
            }
        }
        .onAppear {
            viewModel.fetchMaps()
        }
        .onDisappear {
            viewModel.stop()
        }
    }
}
