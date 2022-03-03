//
//  Networking.swift
//  iosApp
//
//  Created by GERARD on 19/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct Networking: View {
    var networkingUi: NetworkingUi
    var onValidation: (String, String, String, String) -> ()
    var onDismissProfileSheet: () -> ()

    var body: some View {
        Group {
            if (networkingUi.emails.count > 0) {
                ScrollView {
                    LazyVStack(spacing: 10) {
                        ForEach(networkingUi.emails, id: \.self) { email in
                            Text(email)
                                .padding()
                                .frame(maxWidth: .infinity, alignment: .leading)
                        }
                    }
                }
            } else {
                Text("You didn't scan a QR code yet")
            }
        }
        .sheet(isPresented: Binding.constant(networkingUi.showQrCode), onDismiss: {
            onDismissProfileSheet()
        }) {
            ProfileInputView(
                email: networkingUi.userProfileUi.email,
                firstName: networkingUi.userProfileUi.firstName,
                lastName: networkingUi.userProfileUi.lastName,
                company: networkingUi.userProfileUi.company,
                qrCode: networkingUi.userProfileUi.qrCode,
                onValidation: onValidation
            )
        }
    }
}

struct Networking_Previews: PreviewProvider {
    static var previews: some View {
        Networking(
            networkingUi: NetworkingUi.companion.fake,
            onValidation: { _, _, _, _ in },
            onDismissProfileSheet: {}
        )
    }
}
