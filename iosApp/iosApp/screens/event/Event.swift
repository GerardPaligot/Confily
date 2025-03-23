//
//  Event.swift
//  iosApp
//
//  Created by GERARD on 10/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import CodeScanner
import SharedDi

struct Event: View {
    @State var showScanner = false
    @Environment(\.openURL) var openURL
    let event: EventUi
    let linkedin: String?
    let x: String?
    let mastodon: String?
    let bluesky: String?
    let facebook: String?
    let instagram: String?
    let youtube: String?
    let github: String?
    let website: String?
    let barcodeScanned: (String) async -> ()
    let onDisconnectedClicked: () -> ()
    
    init(
        event: EventUi,
        barcodeScanned: @escaping (String) async -> (),
        onDisconnectedClicked: @escaping () -> ()
    ) {
        self.event = event
        self.linkedin = event.eventInfo.socials.first(where: { $0.type == SocialTypeUi.linkedin })?.url
        self.x = event.eventInfo.socials.first(where: { $0.type == SocialTypeUi.x })?.url
        self.mastodon = event.eventInfo.socials.first(where: { $0.type == SocialTypeUi.mastodon })?.url
        self.bluesky = event.eventInfo.socials.first(where: { $0.type == SocialTypeUi.bluesky })?.url
        self.facebook = event.eventInfo.socials.first(where: { $0.type == SocialTypeUi.facebook })?.url
        self.instagram = event.eventInfo.socials.first(where: { $0.type == SocialTypeUi.instagram })?.url
        self.youtube = event.eventInfo.socials.first(where: { $0.type == SocialTypeUi.youtube })?.url
        self.github = event.eventInfo.socials.first(where: { $0.type == SocialTypeUi.github })?.url
        self.website = event.eventInfo.socials.first(where: { $0.type == SocialTypeUi.website })?.url
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
                            qrCode: ImageTransformer().toUIImage(event.ticket!.qrCode!)
                        )
                        .padding()
                    } else if (event.ticket?.qrCode != nil) {
                        TicketQrCodeView(qrCode: ImageTransformer().toUIImage(event.ticket!.qrCode!))
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
                MapsNavigation()
                MenusNavigation()
                TeamMembersNavigation()
                AddressCardView(formattedAddress: event.eventInfo.formattedAddress)
                Link("actionItinerary", destination: URL(string: "maps://?saddr=&daddr=\(event.eventInfo.latitude),\(event.eventInfo.longitude)")!)
            }
            Section(header: Text("titleLinks")) {
                if (event.eventInfo.faqLink != nil) {
                    Link("actionFaq", destination: URL(string: event.eventInfo.faqLink!)!)
                }
                if (event.eventInfo.codeOfConductLink != nil) {
                    Link("actionCoc", destination: URL(string: event.eventInfo.codeOfConductLink!)!)
                }
                if (self.linkedin != nil) {
                    Link("actionLinkedin", destination: URL(string: self.linkedin!)!)
                }
                if (self.x != nil) {
                    Link("actionX", destination: URL(string: self.x!)!)
                }
                if (self.mastodon != nil) {
                    Link("actionMastodon", destination: URL(string: self.mastodon!)!)
                }
                if (self.bluesky != nil) {
                    Link("actionBluesky", destination: URL(string: self.bluesky!)!)
                }
                if (self.facebook != nil) {
                    Link("actionFacebook", destination: URL(string: self.facebook!)!)
                }
                if (self.instagram != nil) {
                    Link("actionInstagram", destination: URL(string: self.instagram!)!)
                }
                if (self.youtube != nil) {
                    Link("actionYouTube", destination: URL(string: self.youtube!)!)
                }
                if (self.github != nil) {
                    Link("actionGitHub", destination: URL(string: self.github!)!)
                }
                if (self.website != nil) {
                    Link("actionWebsite", destination: URL(string: self.website!)!)
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
