//
//  Event.swift
//  iosApp
//
//  Created by GERARD on 10/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct Event<Ticket: View, Menu: View>: View {
    let event: EventUi
    let ticket: () -> (Ticket)
    let menus: () -> (Menu)
    let onLinkClicked: (_: String) -> ()
    let onMapClicked: (_: URL) -> ()
    
    init(
        event: EventUi,
        @ViewBuilder ticket: @escaping () -> (Ticket),
        @ViewBuilder menus: @escaping () -> (Menu),
        onLinkClicked: @escaping (_: String) -> (),
        onMapClicked: @escaping (_: URL) -> ()
    ) {
        self.event = event
        self.ticket = ticket
        self.menus = menus
        self.onLinkClicked = onLinkClicked
        self.onMapClicked = onMapClicked
    }

    var body: some View {
        ScrollView {
            LazyVStack(spacing: 24) {
                Section {
                    EventSectionView(
                        eventInfoUi: event.eventInfo,
                        onLinkClicked: onLinkClicked,
                        ticket: ticket,
                        menus: menus
                    )
                }
                if (event.ticket != nil) {
                    Section {
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
                }
                Section {
                    AddressCardView(
                        formattedAddress: event.eventInfo.formattedAddress,
                        hasGpsLocation: true,
                        mapOnClick: {
                            onMapClicked(URL(string: "maps://?saddr=&daddr=\(event.eventInfo.latitude),\(event.eventInfo.longitude)")!)
                        }
                    )
                }
            }
            .padding(.horizontal, 16)
        }
    }
}

struct Event_Previews: PreviewProvider {
    static var previews: some View {
        Event(
            event: EventUi.companion.fake,
            ticket: {
                ButtonView(text: NSLocalizedString("actionTicketScanner", comment: "")) {
                }
            },
            menus: {
                ButtonView(text: NSLocalizedString("actionMenus", comment: "")) {
                }
            },
            onLinkClicked: { String in },
            onMapClicked: { uri in }
        )
    }
}
