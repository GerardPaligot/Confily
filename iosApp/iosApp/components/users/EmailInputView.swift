//
//  EmailInputView.swift
//  iosApp
//
//  Created by GERARD on 18/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct EmailInputView: View {
    @State var value: String = ""
    var hasQrCode: Bool
    var color: Color = Color.c4hOnBackground
    var onValidation: (String) -> ()
    var onQrCodeClicked: () -> ()
    
    var body: some View {
        HStack(spacing: 16) {
            TextField(
                "Your email address",
                text: $value,
                onCommit: {
                    onValidation(value)
                }
            )
                .textFieldStyle(RoundedBorderTextFieldStyle())
                .lineLimit(1)
            
            Button {
                onValidation(value)
            } label: {
                Image(systemName: "arrow.up.circle")
                    .padding()
            }

            Button {
                onQrCodeClicked()
            } label: {
                Image(systemName: "qrcode")
                    .padding()
            }
        }
    }
}

struct EmailInputView_Previews: PreviewProvider {
    static var previews: some View {
        EmailInputView(
            value: "",
            hasQrCode: true,
            onValidation: { String in },
            onQrCodeClicked: {}
        )
    }
}
