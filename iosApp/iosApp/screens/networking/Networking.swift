//
//  Networking.swift
//  iosApp
//
//  Created by GERARD on 19/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import CodeScanner
import MessageUI
import shared

struct Networking: View {
    var networkingUi: NetworkingUi
    var onShowScanner: () -> ()
    var onExportNetworking: () -> ()
    var onQrcodeScanned: (String) -> ()
    var onValidation: (String, String, String, String) -> ()
    var onNetworkDeleted: (String) -> ()

    var body: some View {
        Group {
            if (networkingUi.userProfileUi != nil) {
                List {
                    if let profileUi = networkingUi.userProfileUi {
                        Section {
                            NavigationLink {
                                ProfileInputView(
                                    email: networkingUi.userProfileUi?.email ?? "",
                                    firstName: networkingUi.userProfileUi?.firstName ?? "",
                                    lastName: networkingUi.userProfileUi?.lastName ?? "",
                                    company: networkingUi.userProfileUi?.company ?? "",
                                    qrCode: networkingUi.userProfileUi?.qrCode ?? nil,
                                    onValidation: onValidation
                                )
                            } label: {
                                ProfileItemView(profileUi: profileUi)
                            }
                        }
                    }
                    Section {
                        ForEach(networkingUi.users, id: \.self) { user in
                            UserItemView(
                                user: user,
                                onNetworkDeleted: onNetworkDeleted
                            )
                            .frame(maxWidth: .infinity, alignment: .leading)
                        }
                    }
                }
                .listStyle(.grouped)
            } else {
                ScrollView {
                    LazyVStack(alignment: .leading, spacing: 24) {
                        Text("textNetworkingEmpty")
                        Text("textNetworkingWarning")
                        Text("textNetworkingHereWeGo")
                        NavigationLink {
                            ProfileInputView(
                                email: "",
                                firstName: "",
                                lastName: "",
                                company: "",
                                qrCode: nil,
                                onValidation: onValidation
                            )
                        } label: {
                            Text("actionProfile")
                        }
                    }
                    .padding([.horizontal])
                }
            }
        }
        .navigationTitle(Text("screenNetworking"))
        .navigationBarTitleDisplayMode(.inline)
        .navigationBarItems(trailing:
            HStack {
                if (networkingUi.userProfileUi != nil) {
                    Button(action: {
                        onShowScanner()
                    }, label: {
                        Image(systemName: "qrcode.viewfinder")
                    })
                    .accessibilityLabel("actionQrcodeScanner")
                }
                if (networkingUi.users.count > 0) {
                    Button {
                        onExportNetworking()
                    } label: {
                        Image(systemName: "square.and.arrow.up")
                    }
                    .disabled(!MailView.canSendMail)
                }
            }
        )
        .sheet(isPresented: Binding.constant(networkingUi.showQrCode)) {
            CodeScannerView(codeTypes: [.qr]) { response in
                if case let .success(result) = response {
                    if (result.string != "") {
                        onQrcodeScanned(result.string)
                    }
                }
            }
        }
    }
}

struct Networking_Previews: PreviewProvider {
    static var previews: some View {
        NavigationView {
            Networking(
                networkingUi: NetworkingUi.companion.fake,
                onShowScanner: {},
                onExportNetworking: {},
                onQrcodeScanned: { _ in },
                onValidation: { _, _, _, _ in },
                onNetworkDeleted: { _ in }
            )
        }
    }
}

struct Empty_Networking_Previews: PreviewProvider {
    static var previews: some View {
        NavigationView {
            Networking(
                networkingUi: NetworkingUi.companion.fake.doCopy(
                    userProfileUi: nil,
                    showQrCode: NetworkingUi.companion.fake.showQrCode,
                    users: []
                ),
                onShowScanner: {},
                onExportNetworking: {},
                onQrcodeScanned: { _ in },
                onValidation: { _, _, _, _ in },
                onNetworkDeleted: { _ in }
            )
        }
    }
}
