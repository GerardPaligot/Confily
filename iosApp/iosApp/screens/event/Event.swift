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
    @Environment(\.openURL) var openURL
    var event: EventUi
    var onFaqClicked: (_: String) -> ()
    var onCoCClicked: (_: String) -> ()
    var onTwitterClicked: (_: String) -> ()
    var onLinkedInClicked: (_: String) -> ()

    var body: some View {
        GeometryReader { geometry in
            let parentWidth = geometry.size.width
            ScrollView {
                LazyVStack(spacing: 8) {
                    Section {
                        EventSectionView(
                            eventInfoUi: event.eventInfo,
                            onFaqClicked: onFaqClicked,
                            onCoCClicked: onCoCClicked,
                            onTwitterClicked: onTwitterClicked,
                            onLinkedInClicked: onLinkedInClicked
                        )
                    }
                    Section {
                        PartnerDividerView(text: NSLocalizedString("titleGold", comment: ""))
                        ForEach(event.partners.golds, id: \.[0].name) { partners in
                            PartnerRowView(partners: partners, parentWidth: parentWidth) { url in
                                if let url2 = URL(string: url) { openURL(url2) }
                            }
                        }
                    }
                    Section {
                        PartnerDividerView(text: NSLocalizedString("titleSilver", comment: ""))
                        ForEach(event.partners.silvers, id: \.[0].name) { partners in
                            PartnerRowView(partners: partners, parentWidth: parentWidth) { url in
                                if let url2 = URL(string: url) { openURL(url2) }
                            }
                        }
                    }
                    Section {
                        PartnerDividerView(text: NSLocalizedString("titleBronze", comment: ""))
                        ForEach(event.partners.bronzes, id: \.[0].name) { partners in
                            PartnerRowView(partners: partners, parentWidth: parentWidth) { url in
                                if let url2 = URL(string: url) { openURL(url2) }
                            }
                        }
                    }
                }
                .padding(.horizontal, 4)
            }
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
