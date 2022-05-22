//
//  EventSectionView.swift
//  iosApp
//
//  Created by GERARD on 10/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct EventSectionView<Menu: View>: View {
    var eventInfoUi: EventInfoUi
    var titleFont: Font = Font.headline
    var subtitleFont: Font = Font.subheadline
    var color: Color = Color.c4hOnBackground
    var onFaqClicked: (_: String) -> ()
    var onCoCClicked: (_: String) -> ()
    var onTwitterClicked: (_: String) -> ()
    var onLinkedInClicked: (_: String) -> ()
    var menus: () -> (Menu)

    var body: some View {
        VStack(spacing: 8) {
            Text(eventInfoUi.name)
                .font(titleFont)
                .foregroundColor(color)
            Text(eventInfoUi.date)
                .font(subtitleFont)
                .foregroundColor(color)
            if (eventInfoUi.twitter != nil || eventInfoUi.linkedin != nil) {
                HStack {
                    if (eventInfoUi.twitter != nil) {
                        SocialItem(
                            iconName: "ic_twitter",
                            text: eventInfoUi.twitter!,
                            onClick: {
                                onTwitterClicked(eventInfoUi.twitterUrl!)
                            }
                        )
                        .accessibility(label: Text(LocalizedStringKey("semanticTwitter \(eventInfoUi.name)")))
                    }
                    if (eventInfoUi.linkedin != nil) {
                        SocialItem(
                            iconName: "ic_linkedin",
                            text: eventInfoUi.linkedin!,
                            onClick: {
                                onLinkedInClicked(eventInfoUi.linkedinUrl!)
                            }
                        )
                        .accessibility(label: Text(LocalizedStringKey("semanticLinkedIn \(eventInfoUi.name)")))
                    }
                }
                .frame(maxWidth: .infinity, alignment: .leading)
            }
            Text(eventInfoUi.address)
                .font(subtitleFont)
                .foregroundColor(color)
                .frame(maxWidth: .infinity, alignment: .leading)
            HStack {
                ButtonView(text: NSLocalizedString("actionFaq", comment: "")) {
                    onFaqClicked(eventInfoUi.faqLink)
                }
                ButtonView(text: NSLocalizedString("actionCoc", comment: "")) {
                    onCoCClicked(eventInfoUi.codeOfConductLink)
                }
            }
            self.menus()
        }
    }
}

struct EventSectionView_Previews: PreviewProvider {
    static var previews: some View {
        EventSectionView(
            eventInfoUi: EventUi.companion.fake.eventInfo,
            onFaqClicked: { String in },
            onCoCClicked: { String in },
            onTwitterClicked: { String in },
            onLinkedInClicked: { String in },
            menus: {
                ButtonView(text: NSLocalizedString("actionMenus", comment: "")) {
                }
            }
        )
    }
}
