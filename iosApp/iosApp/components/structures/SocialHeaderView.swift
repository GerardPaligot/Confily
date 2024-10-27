//
//  SocialHeaderView.swift
//  iosApp
//
//  Created by GERARD on 01/01/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import WrappingHStack
import SharedDi

struct SocialHeaderView: View {
    var title: String
    var pronouns: String? = nil
    var logoUrl: String? = nil
    var xUrl: String? = nil
    var mastodonUrl: String? = nil
    var blueskyUrl: String? = nil
    var facebookUrl: String? = nil
    var instagramUrl: String? = nil
    var youtubeUrl: String? = nil
    var linkedInUrl: String? = nil
    var githubUrl: String? = nil
    var websiteUrl: String? = nil
    var emailUrl: String? = nil
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
                if (xUrl != nil) {
                    Link(destination: URL(string: xUrl!)!) {
                        VStack {
                            Image("ic_x")
                                .renderingMode(.template)
                                .resizable()
                                .scaledToFit()
                                .frame(width: 24, height: 24, alignment: .center)
                            Text("actionX")
                                .font(.caption)
                        }
                        .frame(width: 64)
                    }
                    .buttonStyle(.bordered)
                    .accessibility(label: Text(LocalizedStringKey("semanticX \(title)")))
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
                if (blueskyUrl != nil) {
                    Link(destination: URL(string: blueskyUrl!)!) {
                        VStack {
                            Image("ic_bluesky")
                                .renderingMode(.template)
                                .resizable()
                                .scaledToFit()
                                .frame(width: 24, height: 24, alignment: .center)
                            Text("actionBluesky")
                                .font(.caption)
                        }
                        .frame(width: 64)
                    }
                    .buttonStyle(.bordered)
                    .accessibility(label: Text(LocalizedStringKey("semanticBluesky \(title)")))
                }
                if (facebookUrl != nil) {
                    Link(destination: URL(string: facebookUrl!)!) {
                        VStack {
                            Image("ic_facebook")
                                .renderingMode(.template)
                                .resizable()
                                .scaledToFit()
                                .frame(width: 24, height: 24, alignment: .center)
                            Text("actionFacebook")
                                .font(.caption)
                        }
                        .frame(width: 64)
                    }
                    .buttonStyle(.bordered)
                    .accessibility(label: Text(LocalizedStringKey("semanticFacebook \(title)")))
                }
                if (instagramUrl != nil) {
                    Link(destination: URL(string: instagramUrl!)!) {
                        VStack {
                            Image("ic_instagram")
                                .renderingMode(.template)
                                .resizable()
                                .scaledToFit()
                                .frame(width: 24, height: 24, alignment: .center)
                            Text("actionInstagram")
                                .font(.caption)
                        }
                        .frame(width: 64)
                    }
                    .buttonStyle(.bordered)
                    .accessibility(label: Text(LocalizedStringKey("semanticInstagram \(title)")))
                }
                if (youtubeUrl != nil) {
                    Link(destination: URL(string: youtubeUrl!)!) {
                        VStack {
                            Image("ic_youtube")
                                .renderingMode(.template)
                                .resizable()
                                .scaledToFit()
                                .frame(width: 24, height: 24, alignment: .center)
                            Text("actionYouTube")
                                .font(.caption)
                        }
                        .frame(width: 64)
                    }
                    .buttonStyle(.bordered)
                    .accessibility(label: Text(LocalizedStringKey("semanticYouTube \(title)")))
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
                if (emailUrl != nil) {
                    Link(destination: URL(string: emailUrl!)!) {
                        VStack {
                            Image("mail")
                                .renderingMode(.template)
                                .resizable()
                                .scaledToFit()
                                .frame(width: 24, height: 24, alignment: .center)
                            Text("actionEmail")
                                .font(.caption)
                        }
                        .frame(width: 64)
                    }
                    .buttonStyle(.bordered)
                    .accessibility(label: Text(LocalizedStringKey("semanticEmail \(title)")))
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
            xUrl: PartnerItemUi.companion.fake.socials.first(where: { $0.type == SocialTypeUi.x })?.url,
            mastodonUrl: PartnerItemUi.companion.fake.socials.first(where: { $0.type == SocialTypeUi.mastodon })?.url,
            linkedInUrl: PartnerItemUi.companion.fake.socials.first(where: { $0.type == SocialTypeUi.linkedin })?.url,
            githubUrl: PartnerItemUi.companion.fake.socials.first(where: { $0.type == SocialTypeUi.github })?.url,
            websiteUrl: PartnerItemUi.companion.fake.socials.first(where: { $0.type == SocialTypeUi.website })?.url
        )
    }
}
