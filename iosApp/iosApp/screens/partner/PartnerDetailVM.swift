//
//  PartnerDetailVM.swift
//  iosApp
//
//  Created by GERARD on 30/12/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct PartnerDetailVM: View {
    @ObservedObject var viewModel: PartnerDetailViewModel
    
    init(viewModel: PartnerDetailViewModel) {
        self.viewModel = viewModel
    }

    var body: some View {
        let uiState = viewModel.uiState
        Group {
            switch uiState {
                case .success(let partnerUi):
                    PartnerDetailView(partnerUi: partnerUi)
                case .failure:
                    Text("textError")
                case .loading:
                    Text("textLoading")
            }
        }
        .onAppear {
            viewModel.fetchPartner()
        }
        .onDisappear {
            viewModel.stop()
        }
    }
}
