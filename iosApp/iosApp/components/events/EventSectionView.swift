//
//  EventSectionView.swift
//  iosApp
//
//  Created by GERARD on 10/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct EventSectionView<Ticket: View, Menu: View>: View {
    var eventInfoUi: EventInfoUi
    var titleFont: Font = Font.headline
    var subtitleFont: Font = Font.subheadline
    var color: Color = Color.c4hOnBackground
    var onLinkClicked: (_: String) -> ()
    var ticket: () -> (Ticket)
    var menus: () -> (Menu)

    var body: some View {
        VStack(spacing: 8) {
            SocialHeaderView(
                title: eventInfoUi.name,
                description: eventInfoUi.date,
                twitterUrl: eventInfoUi.twitter,
                linkedInUrl: eventInfoUi.linkedin,
                linkOnClick: onLinkClicked
            )
            HStack(spacing: 8) {
                ButtonView(text: NSLocalizedString("actionFaq", comment: "")) {
                    onLinkClicked(eventInfoUi.faqLink)
                }
                ButtonView(text: NSLocalizedString("actionCoc", comment: "")) {
                    onLinkClicked(eventInfoUi.codeOfConductLink)
                }
            }
            HStack {
                self.ticket()
                Spacer(minLength: 8)
                self.menus()
            }
        }
    }
}

struct EventSectionView_Previews: PreviewProvider {
    static var previews: some View {
        EventSectionView(
            eventInfoUi: EventUi.companion.fake.eventInfo,
            onLinkClicked: { String in },
            ticket: {
                ButtonView(text: NSLocalizedString("actionTicketScanner", comment: "")) {
                }
            },
            menus: {
                ButtonView(text: NSLocalizedString("actionMenus", comment: "")) {
                }
            }
        )
    }
}
