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
    @EnvironmentObject var viewModelFactory: ViewModelFactory
    @State private var showPlayer = false
    @State private var selectedSpeakerId: String?
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
            if (partnerUi.speakers.count > 0) {
                Section {
                    ForEach(partnerUi.speakers, id: \.id) { speaker in
                        Button {
                            selectedSpeakerId = speaker.id
                        } label: {
                            SpeakerItemView(
                                url: speaker.url,
                                title: speaker.name,
                                description: speaker.activity
                            )
                        }
                    }
                } header: {
                    Text("screenSpeakers")
                }
            }
            if (partnerUi.jobs.count > 0) {
                Section {
                    ForEach(self.partnerUi.jobs, id: \.self) { job in
                        Link(destination: URL(string: job.url)!) {
                            JobItemView(jobUi: job)
                        }
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
        .sheet(isPresented: Binding(
            get: { selectedSpeakerId != nil },
            set: { if !$0 { selectedSpeakerId = nil } }
        )) {
            if let speakerId = selectedSpeakerId {
                NavigationView {
                    SpeakerDetailVM(
                        viewModel: viewModelFactory.makeSpeakerViewModel(speakerId: speakerId)
                    )
                    .toolbar {
                        ToolbarItem(placement: .confirmationAction) {
                            Button {
                                selectedSpeakerId = nil
                            } label: {
                                Image(systemName: "xmark.circle.fill")
                                    .foregroundStyle(.gray)
                            }
                        }
                    }
                }
                .modifier(PresentationDetentsModifier())
            }
        }
    }
}

private struct PresentationDetentsModifier: ViewModifier {
    func body(content: Content) -> some View {
        if #available(iOS 16.0, *) {
            content.presentationDetents([.medium, .large])
        } else {
            content
        }
    }
}

struct PartnerDetailView_Previews: PreviewProvider {
    static var previews: some View {
        PartnerDetailView(partnerUi: PartnerUi.companion.fake)
            .environmentObject(ViewModelFactory())
    }
}
