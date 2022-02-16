//
//  Event.swift
//  iosApp
//
//  Created by GERARD on 10/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct Event: View {
    var event: EventUi
    var onFaqClicked: (_: String) -> ()
    var onCoCClicked: (_: String) -> ()
    var onTwitterClicked: (_: String) -> ()
    var onLinkedInClicked: (_: String) -> ()

    var body: some View {
        ScrollView {
            LazyVStack(spacing: 8) {
                EventSectionView(
                    eventInfoUi: event.eventInfo,
                    onFaqClicked: onFaqClicked,
                    onCoCClicked: onCoCClicked,
                    onTwitterClicked: onTwitterClicked,
                    onLinkedInClicked: onLinkedInClicked
                )
                PartnerDividerView(text: "Gold")
                PartnerDividerView(text: "Silver")
                PartnerDividerView(text: "Bronze")
            }
            .padding(.horizontal, 4)
        }
    }
}

struct Event_Previews: PreviewProvider {
    static var previews: some View {
        Event(
            event: EventUi.companion.fake,
            onFaqClicked: { String in },
            onCoCClicked: { String in },
            onTwitterClicked: { String in },
            onLinkedInClicked: { String in }
        )
    }
}
