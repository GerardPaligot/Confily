//
//  PartnersVM.swift
//  iosApp
//
//  Created by GERARD on 22/05/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

enum NavigationPartnerState: Equatable {
    case none
    case partner(String)
}

struct PartnersVM: View {
    @ObservedObject var viewModel: PartnersViewModel
    @State private var navigationState = NavigationPartnerState.none
    let agendaRepository: AgendaRepository

    init(agendaRepository: AgendaRepository) {
        self.agendaRepository = agendaRepository
        self.viewModel = PartnersViewModel(repository: agendaRepository)
    }

    var body: some View {
        let uiState = viewModel.uiState
        NavigationView {
            Group {
                switch uiState {
                    case .success(let partners):
                        Partners(
                            partners: partners,
                            partnerItem: { partner, size in
                                NavigationLink(isActive: Binding.constant(self.navigationState == .partner(partner.id))) {
                                    PartnerDetailVM(
                                        agendaRepository: self.agendaRepository,
                                        partnerId: partner.id
                                    )
                                } label: {
                                    EmptyView()
                                }
                                .accessibility(hidden: true)
                                Button {
                                    self.navigationState = NavigationPartnerState.partner(partner.id)
                                } label: {
                                    PartnerItemView(
                                        id: partner.id,
                                        name: partner.name,
                                        logoUrl: partner.logoUrl
                                    )
                                }
                                .frame(width: size, height: size)
                                .background(Color.white)
                                .clipShape(RoundedRectangle(cornerRadius: 8))
                                .onAppear {
                                    self.navigationState = NavigationPartnerState.none
                                }
                            }
                        )
                    case .failure:
                        Text("textError")
                    case .loading:
                        Text("textLoading")
                }
            }
            .navigationTitle(Text("screenPartners"))
            .navigationBarTitleDisplayMode(.inline)
        }
        .onAppear {
            viewModel.fetchPartners()
        }
        .onDisappear {
            viewModel.stop()
        }
    }
}
