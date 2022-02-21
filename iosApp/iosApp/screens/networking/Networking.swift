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
    var userProfile: UserProfileUi
    var onValidation: (String) -> ()
    var onQrCodeClicked: () -> ()

    var body: some View {
        ScrollView {
            LazyVStack(spacing: 10) {
                EmailInputView(
                    value: userProfile.email,
                    hasQrCode: userProfile.hasQrCode,
                    onValidation: onValidation,
                    onQrCodeClicked: onQrCodeClicked
                )
                ForEach(userProfile.emails, id: \.self) { email in
                    Text(email)
                        .padding()
                        .frame(maxWidth: .infinity, alignment: .leading)
                }
            }
        }
        .sheet(isPresented: Binding.constant(userProfile.showQrCode)) {
            if let uiImage = userProfile.qrcode {
                Image(uiImage: uiImage)
            }
        }
    }
}

struct Networking_Previews: PreviewProvider {
    static var previews: some View {
        Networking(
            userProfile: UserProfileUi.companion.fake,
            onValidation: { String in },
            onQrCodeClicked: {}
        )
    }
}
