//
//  PartnerDetailVM.swift
//  iosApp
//
//  Created by GERARD on 30/12/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct PartnerDetailVM: View {
    @ObservedObject var viewModel: PartnerDetailViewModel
    @Environment(\.openURL) var openURL
    let agendaRepository: AgendaRepository
    let partnerId: String
    
    init(agendaRepository: AgendaRepository, partnerId: String) {
        self.agendaRepository = agendaRepository
        self.viewModel = PartnerDetailViewModel(repository: agendaRepository)
        self.partnerId = partnerId
    }

    var body: some View {
        let uiState = viewModel.uiState
        Group {
            switch uiState {
                case .success(let partnerUi):
                    PartnerDetailView(
                        partnerUi: partnerUi,
                        linkOnClick: { url in
                            if let url2 = URL(string: url) { openURL(url2) }
                        },
                        mapOnClick: { url in
                            if UIApplication.shared.canOpenURL(url) {
                                UIApplication.shared.open(url, options: [:], completionHandler: nil)
                            }
                        }
                    )
                case .failure:
                    Text("textError")
                case .loading:
                    Text("textLoading")
            }
        }
        .onAppear {
            viewModel.fetchPartner(partnerId: partnerId)
        }
        .onDisappear {
            viewModel.stop()
        }
    }
}
