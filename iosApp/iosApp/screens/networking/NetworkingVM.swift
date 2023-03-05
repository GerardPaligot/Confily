//
//  NetworkingVM.swift
//  iosApp
//
//  Created by GERARD on 19/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct NetworkingVM: View {
    @ObservedObject var viewModel: NetworkingViewModel
    
    init(viewModel: NetworkingViewModel) {
        self.viewModel = viewModel
    }
    
    var body: some View {
        let uiState = viewModel.uiState
        NavigationView {
            Group {
                switch uiState {
                    case .success(let networkingUi):
                    Networking(
                        networkingUi: networkingUi,
                        onShowScanner: {
                            viewModel.showQrcode()
                        },
                        onQrcodeScanned: { barcode in
                            Task {
                                viewModel.saveNetworkingProfile(text: barcode)
                            }
                        },
                        onValidation: { email, firstName, lastName, company in
                            Task {
                                viewModel.saveProfile(email: email, firstName: firstName, lastName: lastName, company: company)
                            }
                        },
                        onNetworkDeleted: { email in
                            Task {
                                viewModel.deleteNetworkProfile(email: email)
                            }
                        }
                    )
                    case .failure(_):
                        Text("textError")
                    case .loading:
                        Text("textLoading")
                }
            }
        }
        .onAppear {
            viewModel.fetchNetworking()
        }
        .onDisappear {
            viewModel.stop()
        }
    }
}
