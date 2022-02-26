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
    var onValidation: (String) -> ()
    var onQrCodeClicked: () -> ()

    var body: some View {
        ScrollView {
            LazyVStack(spacing: 10) {
                EmailInputView(
                    value: networkingUi.email,
                    hasQrCode: networkingUi.hasQrCode,
                    onValidation: onValidation,
                    onQrCodeClicked: onQrCodeClicked
                )
                ForEach(networkingUi.emails, id: \.self) { email in
                    Text(email)
                        .padding()
                        .frame(maxWidth: .infinity, alignment: .leading)
                }
            }
        }
        .sheet(isPresented: Binding.constant(networkingUi.showQrCode)) {
            if let uiImage = networkingUi.qrcode {
                Image(uiImage: uiImage)
            }
        }
    }
}

struct Networking_Previews: PreviewProvider {
    static var previews: some View {
        Networking(
            networkingUi: NetworkingUi.companion.fake,
            onValidation: { String in },
            onQrCodeClicked: {}
        )
    }
}
