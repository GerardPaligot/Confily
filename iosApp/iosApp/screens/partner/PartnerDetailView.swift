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
    let partnerUi: PartnerItemUi

    var body: some View {
        List {
            Section {
                HStack {
                    Spacer()
                    SocialHeaderView(
                        title: partnerUi.name,
                        logoUrl: partnerUi.logoUrl,
                        twitterUrl: partnerUi.twitterUrl,
                        linkedInUrl: partnerUi.linkedinUrl,
                        websiteUrl: partnerUi.siteUrl
                    )
                    Spacer()
                }
                Text(partnerUi.description_)
                    .font(.callout)
            }
            if (partnerUi.jobs.count > 0) {
                Section {
                    ForEach(self.partnerUi.jobs, id: \.self) { job in
                        Link(destination: URL(string: job.url)!) {
                            JobItemView(jobUi: job)
                        }
                        .buttonStyle(.plain)
                    }
                } header: {
                    Text("titleJobs")
                }
            }
            if (partnerUi.formattedAddress != nil) {
                Section {
                    AddressCardView(formattedAddress: partnerUi.formattedAddress!)
                    if (partnerUi.latitude != nil && partnerUi.longitude != nil) {
                        Link("actionItinerary", destination: URL(string: "maps://?saddr=&daddr=\(partnerUi.latitude ?? 0),\(partnerUi.longitude ?? 0)")!)
                    }
                }
            }
        }
        .listStyle(GroupedListStyle())
    }
}

struct PartnerDetailView_Previews: PreviewProvider {
    static var previews: some View {
        PartnerDetailView(partnerUi: PartnerItemUi.companion.fake)
    }
}
