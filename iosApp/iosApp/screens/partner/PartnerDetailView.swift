//
//  PartnerDetailView.swift
//  iosApp
//
//  Created by GERARD on 30/12/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct PartnerDetailView: View {
    var partnerUi: PartnerItemUi
    var linkOnClick: (_: String) -> ()
    var mapOnClick: (_: URL) -> ()

    var body: some View {
        let hasGpsLocation = partnerUi.latitude != nil && partnerUi.longitude != nil
        ScrollView {
            LazyVStack(alignment: .leading, spacing: 16) {
                PartnerDetailSectionView(
                    partnerUi: partnerUi,
                    twitterOnClick: linkOnClick,
                    linkedinOnClick: linkOnClick,
                    websiteOnClick: linkOnClick
                )
                if (partnerUi.formattedAddress != nil) {
                    VStack(alignment: .leading) {
                        Text("titlePlanPartner")
                            .font(.headline)
                            .fontWeight(.bold)
                        AddressCardView(
                            formattedAddress: partnerUi.formattedAddress!,
                            hasGpsLocation: hasGpsLocation,
                            mapOnClick: {
                                mapOnClick(URL(string: "maps://?saddr=&daddr=\(partnerUi.latitude ?? 0),\(partnerUi.longitude ?? 0)")!)
                            }
                        )
                    }
                }
                if (partnerUi.twitterMessage != nil || partnerUi.linkedinMessage != nil) {
                    Text("titleCommunication")
                        .font(.headline)
                        .fontWeight(.bold)
                    if (partnerUi.twitterMessage != nil) {
                        SocialCardView(
                            title: LocalizedStringKey("titleCommunicationTwitter").stringValue(),
                            message: partnerUi.twitterMessage!,
                            icon: "ic_twitter"
                        )
                    }
                    if (partnerUi.linkedinMessage != nil) {
                        SocialCardView(
                            title: LocalizedStringKey("titleCommunicationLinkedin").stringValue(),
                            message: partnerUi.linkedinMessage!,
                            icon: "ic_linkedin"
                        )
                    }
                }
            }
            .padding([.horizontal], 16)
        }
    }
}

struct PartnerDetailView_Previews: PreviewProvider {
    static var previews: some View {
        PartnerDetailView(
            partnerUi: PartnerItemUi.companion.fake,
            linkOnClick: { url in },
            mapOnClick: { url in }
        )
    }
}
