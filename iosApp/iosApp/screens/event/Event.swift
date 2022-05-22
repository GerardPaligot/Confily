//
//  Event.swift
//  iosApp
//
//  Created by GERARD on 10/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct Event<Menu: View>: View {
    let event: EventUi
    let menus: () -> (Menu)
    let onFaqClicked: (_: String) -> ()
    let onCoCClicked: (_: String) -> ()
    let onTwitterClicked: (_: String) -> ()
    let onLinkedInClicked: (_: String) -> ()
    
    init(
        event: EventUi,
        @ViewBuilder menus: @escaping () -> (Menu),
        onFaqClicked: @escaping (_: String) -> (),
        onCoCClicked: @escaping (_: String) -> (),
        onTwitterClicked: @escaping (_: String) -> (),
        onLinkedInClicked: @escaping (_: String) -> ()
    ) {
        self.event = event
        self.menus = menus
        self.onFaqClicked = onFaqClicked
        self.onCoCClicked = onCoCClicked
        self.onTwitterClicked = onTwitterClicked
        self.onLinkedInClicked = onLinkedInClicked
    }

    var body: some View {
        ScrollView {
            LazyVStack(spacing: 8) {
                Section {
                    EventSectionView(
                        eventInfoUi: event.eventInfo,
                        onFaqClicked: onFaqClicked,
                        onCoCClicked: onCoCClicked,
                        onTwitterClicked: onTwitterClicked,
                        onLinkedInClicked: onLinkedInClicked,
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
            }
            .padding(.horizontal, 4)
        }
    }
}

struct Event_Previews: PreviewProvider {
    static var previews: some View {
        Event(
            event: EventUi.companion.fake,
            menus: {
                ButtonView(text: NSLocalizedString("actionMenus", comment: "")) {
                }
            },
            onFaqClicked: { String in },
            onCoCClicked: { String in },
            onTwitterClicked: { String in },
            onLinkedInClicked: { String in }
        )
    }
}
