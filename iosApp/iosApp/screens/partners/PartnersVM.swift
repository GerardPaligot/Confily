//
//  PartnersVM.swift
//  iosApp
//
//  Created by GERARD on 22/05/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

enum NavigationPartnerState: Equatable {
    case none
    case partner(String)
}

struct PartnersVM: View {
    @EnvironmentObject var viewModelFactory: ViewModelFactory
    @ObservedObject var viewModel: PartnersViewModel
    @State private var navigationState = NavigationPartnerState.none

    init(viewModel: PartnersViewModel) {
        self.viewModel = viewModel
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
                                        viewModel: viewModelFactory.makePartnerDetailViewModel(partnerId: partner.id)
                                    )
                                } label: {
                                    EmptyView()
                                }
                                .accessibility(hidden: true)
                                Button {
                                    self.navigationState = NavigationPartnerState.partner(partner.id)
                                } label: {
                                    RemoteImage(
                                        url: partner.logoUrl,
                                        description: partner.name,
                                        id: partner.id
                                    )
                                    .padding()
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
