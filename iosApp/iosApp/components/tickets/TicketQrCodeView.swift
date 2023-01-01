//
//  TicketQrCodeView.swift
//  iosApp
//
//  Created by GERARD on 17/05/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct TicketQrCodeView: View {
    var qrCode: UIImage
    
    var body: some View {
        Image(uiImage: qrCode)
            .resizable()
            .frame(width: 250, height: 250, alignment: .center)
            .padding()
            .background(Color.c4hSurface)
            .cornerRadius(16)
            .overlay(
                RoundedRectangle(cornerRadius: 16)
                    .stroke(.black, lineWidth: 2)
            )
            .accessibilityLabel("semanticTicketQrcode")
    }
}

