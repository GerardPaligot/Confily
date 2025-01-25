//
//  PartnerDetailView.swift
//  iosApp
//
//  Created by GERARD on 30/12/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import SharedDi

struct PartnerDetailView: View {
    @State private var showPlayer = false
    let partnerUi: PartnerUi

    var body: some View {
        List {
            Section {
                HStack {
                    Spacer()
                    SocialHeaderView(
                        title: partnerUi.name,
                        logoUrl: partnerUi.logoUrl,
                        xUrl: partnerUi.socials.first(where: { $0.type == SocialTypeUi.x })?.url,
                        mastodonUrl: partnerUi.socials.first(where: { $0.type == SocialTypeUi.mastodon })?.url,
                        blueskyUrl: partnerUi.socials.first(where: { $0.type == SocialTypeUi.bluesky })?.url,
                        facebookUrl: partnerUi.socials.first(where: { $0.type == SocialTypeUi.facebook })?.url,
                        instagramUrl: partnerUi.socials.first(where: { $0.type == SocialTypeUi.instagram })?.url,
                        youtubeUrl: partnerUi.socials.first(where: { $0.type == SocialTypeUi.youtube })?.url,
                        linkedInUrl: partnerUi.socials.first(where: { $0.type == SocialTypeUi.linkedin })?.url,
                        websiteUrl: partnerUi.socials.first(where: { $0.type == SocialTypeUi.website })?.url,
                        emailUrl: partnerUi.socials.first(where: { $0.type == SocialTypeUi.email })?.url
                    )
                    Spacer()
                }
                Text(partnerUi.description_)
                    .font(.callout)
                if (self.partnerUi.videoUrl != nil) {
                    NavigationLink(isActive: $showPlayer) {
                        VideoPlayerUIView(url: self.partnerUi.videoUrl!)
                    } label: {
                        Text("Video presentation")
                    }
                }
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
        /*
        .sheet(isPresented: $isPlayerSheetPresented) {
            VideoPlayerUIView(url: self.partnerUi.videoUrl!)
        }
        */
    }
}

struct PartnerDetailView_Previews: PreviewProvider {
    static var previews: some View {
        PartnerDetailView(partnerUi: PartnerUi.companion.fake)
    }
}
