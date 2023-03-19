//
//  SocialHeaderView.swift
//  iosApp
//
//  Created by GERARD on 01/01/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import WrappingHStack
import shared

struct SocialHeaderView: View {
    var title: String
    var pronouns: String? = nil
    var logoUrl: String? = nil
    var twitterUrl: String? = nil
    var mastodonUrl: String? = nil
    var linkedInUrl: String? = nil
    var githubUrl: String? = nil
    var websiteUrl: String? = nil
    var id: String? = nil

    var body: some View {
        VStack(alignment: .center, spacing: 24) {
            VStack {
                if (logoUrl != nil) {
                    RemoteImage(
                        url: logoUrl!,
                        description: nil,
                        id: id
                    )
                    .padding()
                    .frame(width: 128, height: 128)
                    .background(Color.white)
                    .clipShape(RoundedRectangle(cornerRadius: 8))
                }
                HStack(alignment: .bottom, spacing: 16) {
                    Text(title)
                        .foregroundColor(Color.c4hOnBackground)
                        .font(Font.title2)
                    if (pronouns != nil) {
                        Text(pronouns!)
                            .foregroundColor(.secondary)
                            .font(Font.callout)
                    }
                }
            }
            WrappingHStack(alignment: .center, lineSpacing: 8) {
                if (twitterUrl != nil) {
                    Link(destination: URL(string: twitterUrl!)!) {
                        VStack {
                            Image("ic_twitter")
                                .renderingMode(.template)
                                .resizable()
                                .scaledToFit()
                                .frame(width: 24, height: 24, alignment: .center)
                            Text("actionTwitter")
                                .font(.caption)
                        }
                        .frame(width: 64)
                    }
                    .buttonStyle(.bordered)
                    .accessibility(label: Text(LocalizedStringKey("semanticTwitter \(title)")))
                }
                if (mastodonUrl != nil) {
                    Link(destination: URL(string: mastodonUrl!)!) {
                        VStack {
                            Image("ic_mastodon")
                                .renderingMode(.template)
                                .resizable()
                                .scaledToFit()
                                .frame(width: 24, height: 24, alignment: .center)
                            Text("actionMastodon")
                                .font(.caption)
                        }
                        .frame(width: 64)
                    }
                    .buttonStyle(.bordered)
                    .accessibility(label: Text(LocalizedStringKey("semanticMastodon \(title)")))
                }
                if (linkedInUrl != nil) {
                    Link(destination: URL(string: linkedInUrl!)!) {
                        VStack {
                            Image("ic_linkedin")
                                .renderingMode(.template)
                                .resizable()
                                .scaledToFit()
                                .frame(width: 24, height: 24, alignment: .center)
                            Text("actionLinkedin")
                                .font(.caption)
                        }
                        .frame(width: 64)
                    }
                    .buttonStyle(.bordered)
                    .accessibility(label: Text(LocalizedStringKey("semanticLinkedIn \(title)")))
                }
                if (githubUrl != nil) {
                    Link(destination: URL(string: githubUrl!)!) {
                        VStack {
                            Image("ic_github")
                                .renderingMode(.template)
                                .resizable()
                                .scaledToFit()
                                .frame(width: 24, height: 24, alignment: .center)
                            Text("actionGitHub")
                                .font(.caption)
                        }
                        .frame(width: 64)
                    }
                    .buttonStyle(.bordered)
                    .accessibility(label: Text(LocalizedStringKey("semanticGitHub \(title)")))
                }
                if (websiteUrl != nil) {
                    Link(destination: URL(string: websiteUrl!)!) {
                        VStack {
                            Image("ic_website")
                                .renderingMode(.template)
                                .resizable()
                                .scaledToFit()
                                .frame(width: 24, height: 24, alignment: .center)
                            Text("actionWebsite")
                                .font(.caption)
                        }
                        .frame(width: 64)
                    }
                    .buttonStyle(.bordered)
                    .accessibility(label: Text(LocalizedStringKey("semanticWebsite \(title)")))
                }
            }
        }
    }
}

struct SocialHeaderView_Previews: PreviewProvider {
    static var previews: some View {
        SocialHeaderView(
            title: PartnerItemUi.companion.fake.name,
            pronouns: SpeakerUi.companion.fake.pronouns,
            logoUrl: PartnerItemUi.companion.fake.logoUrl,
            twitterUrl: PartnerItemUi.companion.fake.twitterUrl,
            mastodonUrl: SpeakerUi.companion.fake.mastodonUrl,
            linkedInUrl: PartnerItemUi.companion.fake.linkedinUrl,
            githubUrl: SpeakerUi.companion.fake.githubUrl,
            websiteUrl: PartnerItemUi.companion.fake.siteUrl
        )
    }
}
