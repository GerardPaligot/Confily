//
//  EventSectionView.swift
//  iosApp
//
//  Created by GERARD on 10/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct EventSectionView: View {
    var eventInfoUi: EventInfoUi
    var titleFont: Font = Font.headline
    var subtitleFont: Font = Font.subheadline
    var color: Color = Color.c4hOnBackground
    var onFaqClicked: (_: String) -> ()
    var onCoCClicked: (_: String) -> ()
    var onTwitterClicked: (_: String) -> ()
    var onLinkedInClicked: (_: String) -> ()

    var body: some View {
        VStack {
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
                    }
                    if (eventInfoUi.linkedin != nil) {
                        SocialItem(
                            iconName: "ic_linkedin",
                            text: eventInfoUi.linkedin!,
                            onClick: {
                                onLinkedInClicked(eventInfoUi.linkedinUrl!)
                            }
                        )
                    }
                }
                .frame(maxWidth: .infinity, alignment: .leading)
            }
            Text(eventInfoUi.address)
                .font(subtitleFont)
                .foregroundColor(color)
                .frame(maxWidth: .infinity, alignment: .leading)
            HStack {
                ButtonView(text: "FAQ") {
                    onFaqClicked(eventInfoUi.faqLink)
                }
                ButtonView(text: "CoC") {
                    onCoCClicked(eventInfoUi.codeOfConductLink)
                }
            }
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
            onLinkedInClicked: { String in }
        )
    }
}
