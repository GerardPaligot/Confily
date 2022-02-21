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
    @ObservedObject var viewModel: UserProfileViewModel
    @State private var isPresentingScanner = false
    @State private var scannedCode: String?
    
    init(userRepository: UserRepository) {
        self.viewModel = UserProfileViewModel(repository: userRepository)
    }
    
    var body: some View {
        let uiState = viewModel.uiState
        NavigationView {
            Group {
                switch uiState {
                    case .success(let userProfileUi):
                    Networking(
                        userProfile: userProfileUi,
                        onValidation: { email in
                            viewModel.fetchNewEmailQrCode(email: email)
                        },
                        onQrCodeClicked: {
                            viewModel.displayQrCode()
                        }
                    )
                    case .failure(_):
                        Text("Something wrong happened")
                    case .loading:
                        Text("Loading")
                }
            }
            .navigationTitle(Text("Networking"))
            .navigationBarTitleDisplayMode(.inline)
            .navigationBarItems(trailing:
                Button(action: {
                    isPresentingScanner = true
                }, label: {
                    Image(systemName: "qrcode.viewfinder")
                })
            )
            .sheet(isPresented: $isPresentingScanner) {
                CodeScannerView(codeTypes: [.qr]) { response in
                    if case let .success(result) = response {
                        scannedCode = result.string
                        isPresentingScanner = false
                    }
                }
            }
            .onAppear {
                viewModel.fetchEmailQrCode()
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
