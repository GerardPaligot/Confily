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
                            viewModel.fetchProfile(email: email, firstName: firstName, lastName: lastName, company: company)
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
                    Button(action: {
                        viewModel.displayQrCode()
                    }, label: {
                        Image(systemName: "qrcode")
                    })
                }
            )
            .sheet(isPresented: $isPresentingScanner) {
                CodeScannerView(codeTypes: [.qr]) { response in
                    if case let .success(result) = response {
                        if (result.string != "") {
                            viewModel.saveNetworkingProfile(text: result.string)
                            isPresentingScanner = false
                        }
                    }
                }
            }
            .onAppear {
                viewModel.fetchProfile()
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
