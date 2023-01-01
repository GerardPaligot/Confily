//
//  TicketDetailedView.swift
//  iosApp
//
//  Created by GERARD on 17/05/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct TicketDetailedView: View {
    var ticket: TicketInfoUi
    var qrCode: UIImage?
    
    var body: some View {
        ZStack {
            VStack {
                VStack(spacing: 12) {
                    HStack {
                        Text("textTicketFirstname")
                        Spacer().frame(width: .infinity)
                        Text(ticket.firstName)
                    }
                    .accessibilityElement(children: .combine)
                    HStack {
                        Text("textTicketLastname")
                        Spacer().frame(width: .infinity)
                        Text(ticket.lastName)
                    }
                    .accessibilityElement(children: .combine)
                }
                .padding()
                Line()
                    .stroke(style: StrokeStyle(lineWidth: 1, dash: [5]))
                    .frame(height: 1)
                VStack(spacing: 12) {
                    Text(ticket.id)
                        .accessibilityLabel(LocalizedStringKey("semanticTicketId \(ticket.id)"))
                    if let uiImage = qrCode {
                        Image(uiImage: uiImage)
                            .resizable()
                            .frame(width: 250, height: 250, alignment: .center)
                            .accessibilityLabel("semanticTicketQrcode")
                    }
                }
                .padding()
            }
        }
        .background(Color.c4hSurface)
        .cornerRadius(16)
        .overlay(
            RoundedRectangle(cornerRadius: 16)
                .stroke(.black, lineWidth: 2)
        )
    }
}

struct Line: Shape {
    func path(in rect: CGRect) -> Path {
        var path = Path()
        path.move(to: CGPoint(x: 0, y: 0))
        path.addLine(to: CGPoint(x: rect.width, y: 0))
        return path
    }
}

struct TicketDetailedView_Previews: PreviewProvider {
    static var previews: some View {
        TicketDetailedView(
            ticket: TicketUi.companion.fake.info!
        )
        .padding()
    }
}
