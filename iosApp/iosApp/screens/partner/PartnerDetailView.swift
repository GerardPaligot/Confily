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
                SocialHeaderView(
                    title: partnerUi.name,
                    description: partnerUi.description_,
                    logoUrl: partnerUi.logoUrl,
                    twitterUrl: partnerUi.twitterUrl,
                    linkedInUrl: partnerUi.linkedinUrl,
                    websiteUrl: partnerUi.siteUrl,
                    linkOnClick: linkOnClick
                )
                if (partnerUi.jobs.count > 0) {
                    Text("titleJobs")
                        .font(.headline)
                        .fontWeight(.bold)
                    ForEach(self.partnerUi.jobs, id: \.self) { job in
                        JobItemView(
                            jobUi: job,
                            onClick: linkOnClick
                        )
                    }
                }
                if (partnerUi.formattedAddress != nil) {
                    VStack(alignment: .leading) {
                        Text("titlePlanPartner")
                            .font(.headline)
                            .fontWeight(.bold)
                        /*
                         FIXME
                         mapOnClick(URL(string: "maps://?saddr=&daddr=\(partnerUi.latitude ?? 0),\(partnerUi.longitude ?? 0)")!)
                         */
                        AddressCardView(formattedAddress: partnerUi.formattedAddress!)
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
