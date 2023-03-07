//
//  NetworkingVM.swift
//  iosApp
//
//  Created by GERARD on 19/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared
import MessageUI

struct MailingState {
    var export: ComposeMailData
    var isPresenting: Bool
}

struct NetworkingVM: View {
    @ObservedObject var viewModel: NetworkingViewModel
    @State private var showMailView = false
    @State private var mailingState = MailingState(
        export: ComposeMailData.empty,
        isPresenting: false
    )
    
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
                        onExportNetworking: {
                            if MFMailComposeViewController.canSendMail() {
                                mailingState = MailingState(
                                    export: viewModel.exportNetworking(),
                                    isPresenting: true
                                )
                            }
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
        .sheet(isPresented: self.$mailingState.isPresenting) {
            MailView(
                data: self.$mailingState.export,
                callback: nil
            )
        }
        .onAppear {
            viewModel.fetchNetworking()
        }
        .onDisappear {
            viewModel.stop()
        }
    }
}
