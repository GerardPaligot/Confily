//
//  Event.swift
//  iosApp
//
//  Created by GERARD on 10/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import CodeScanner
import shared

struct Event: View {
    @State var showScanner = false
    @Environment(\.openURL) var openURL
    let event: EventUi
    let barcodeScanned: (String) async -> ()
    let onDisconnectedClicked: () -> ()
    
    init(
        event: EventUi,
        barcodeScanned: @escaping (String) async -> (),
        onDisconnectedClicked: @escaping () -> ()
    ) {
        self.event = event
        self.barcodeScanned = barcodeScanned
        self.onDisconnectedClicked = onDisconnectedClicked
    }

    var body: some View {
        List {
            Section {
                if (event.ticket != nil) {
                    if (event.ticket?.info != nil) {
                        TicketDetailedView(
                            ticket: (event.ticket?.info)!,
                            qrCode: event.ticket!.qrCode
                        )
                        .padding()
                    } else if (event.ticket?.qrCode != nil) {
                        TicketQrCodeView(qrCode: (event.ticket?.qrCode)!)
                            .padding()
                    }
                }
                NavigationLink(isActive: $showScanner) {
                    CodeScannerView(codeTypes: [.qr]) { response in
                        if case let .success(result) = response {
                            if (result.string != "") {
                                Task {
                                    await self.barcodeScanned(result.string)
                                    showScanner = false
                                }
                            }
                        }
                    }
                } label: {
                    Text("actionTicketScanner")
                }
                MenusNavigation()
                AddressCardView(formattedAddress: event.eventInfo.formattedAddress)
                Link("actionItinerary", destination: URL(string: "maps://?saddr=&daddr=\(event.eventInfo.latitude),\(event.eventInfo.longitude)")!)
            }
            Section(header: Text("titleLinks")) {
                Link("actionFaq", destination: URL(string: event.eventInfo.faqLink)!)
                Link("actionCoc", destination: URL(string: event.eventInfo.codeOfConductLink)!)
                if (event.eventInfo.twitterUrl != nil) {
                    Link("actionTwitter", destination: URL(string: event.eventInfo.twitterUrl!)!)
                }
                if (event.eventInfo.linkedinUrl != nil) {
                    Link("actionLinkedin", destination: URL(string: event.eventInfo.linkedinUrl!)!)
                }
            }
            Section {
                Button {
                    onDisconnectedClicked()
                } label: {
                    Text("actionChangeEvent")
                }
            }
        }
        .listStyle(GroupedListStyle())
        .navigationTitle(Text(self.event.eventInfo.name))
        .navigationBarTitleDisplayMode(.inline)
    }
}

struct Event_Previews: PreviewProvider {
    static var previews: some View {
        NavigationView {
            Event(
                event: EventUi.companion.fake,
                barcodeScanned: { barcode in },
                onDisconnectedClicked: {}
            ).environmentObject(ViewModelFactory())
        }
    }
}
