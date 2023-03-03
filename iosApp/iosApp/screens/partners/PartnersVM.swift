//
//  PartnersVM.swift
//  iosApp
//
//  Created by GERARD on 22/05/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct PartnersVM: View {
    @ObservedObject var viewModel: PartnersViewModel

    var body: some View {
        let uiState = viewModel.uiState
        NavigationView {
            Group {
                switch uiState {
                    case .success(let partners):
                        Partners(partners: partners)
                    case .failure:
                        Text("textError")
                    case .loading:
                        Text("textLoading")
                }
            }
        }
        .onAppear {
            viewModel.fetchPartners()
        }
        .onDisappear {
            viewModel.stop()
        }
    }
}
