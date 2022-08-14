//
//  NetworkingVM.swift
//  iosApp
//
//  Created by GERARD on 19/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import CodeScanner
import shared

struct NetworkingVM: View {
    @ObservedObject var viewModel: NetworkingViewModel
    @State private var isPresentingScanner = false
    
    init(userRepository: UserRepository) {
        self.viewModel = NetworkingViewModel(repository: userRepository)
    }
    
    var body: some View {
        let uiState = viewModel.uiState
        NavigationView {
            Group {
                switch uiState {
                    case .success(let networkingUi):
                    Networking(
                        networkingUi: networkingUi,
                        onValidation: { email, firstName, lastName, company in
                            viewModel.saveProfile(email: email, firstName: firstName, lastName: lastName, company: company)
                        },
                        onDismissProfileSheet: {
                            viewModel.closeQrCode()
                        },
                        onNetworkDeleted: { email in
                            viewModel.deleteNetworkProfile(email: email)
                        }
                    )
                    case .failure(_):
                        Text("textError")
                    case .loading:
                        Text("textLoading")
                }
            }
            .navigationTitle(Text("screenNetworking"))
            .navigationBarTitleDisplayMode(.inline)
            .navigationBarItems(trailing:
                HStack {
                    Button(action: {
                        isPresentingScanner = true
                    }, label: {
                        Image(systemName: "qrcode.viewfinder")
                    })
                    .accessibilityLabel("actionQrcodeScanner")
                    Button(action: {
                        viewModel.displayQrCode()
                    }, label: {
                        Image(systemName: "qrcode")
                    })
                    .accessibilityLabel("actionQrcodeGenerator")
                }
            )
            .sheet(isPresented: $isPresentingScanner) {
                CodeScannerView(codeTypes: [.qr]) { response in
                    if case let .success(result) = response {
                        if (result.string != "") {
                            viewModel.saveNetworkingProfile(text: result.string) { hasInserted in
                                if (hasInserted) {
                                    isPresentingScanner = false
                                }
                            }
                        }
                    }
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
