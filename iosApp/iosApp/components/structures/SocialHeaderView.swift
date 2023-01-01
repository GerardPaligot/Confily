//
//  SocialHeaderView.swift
//  iosApp
//
//  Created by GERARD on 01/01/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct SocialHeaderView: View {
    var title: String
    var description: String
    var logoUrl: String? = nil
    var twitterUrl: String? = nil
    var linkedInUrl: String? = nil
    var githubUrl: String? = nil
    var websiteUrl: String? = nil
    var id: String? = nil
    var hasLogoPadding: Bool = true
    var color: Color = Color.c4hOnBackground
    var font: Font = Font.subheadline
    var linkOnClick: (_: String) -> ()

    var body: some View {
        HStack(alignment: .top, spacing: 8) {
            if (logoUrl != nil) {
                RemoteImage(
                    url: logoUrl!,
                    description: nil,
                    id: id
                )
                .if(self.hasLogoPadding, transform: { view in
                    view.padding()
                })
                .frame(width: 128, height: 128)
                .background(Color.white)
                .clipShape(RoundedRectangle(cornerRadius: 8))
            }
            VStack(alignment: .leading, spacing: 8) {
                Text(title)
                    .foregroundColor(color)
                    .font(font)
                    .fontWeight(.bold)
                Text(description)
                    .foregroundColor(color)
                    .font(.caption)
            }
            Spacer()
            VStack {
                if (twitterUrl != nil) {
                    Button {
                        linkOnClick(twitterUrl!)
                    } label: {
                        Image("ic_twitter")
                            .renderingMode(.template)
                            .resizable()
                            .scaledToFit()
                            .frame(width: 24, height: 24, alignment: .center)
                    }
                    .accessibility(label: Text(LocalizedStringKey("semanticTwitter \(title)")))
                }
                if (linkedInUrl != nil) {
                    Button {
                        linkOnClick(linkedInUrl!)
                    } label: {
                        Image("ic_linkedin")
                            .renderingMode(.template)
                            .resizable()
                            .scaledToFit()
                            .frame(width: 24, height: 24, alignment: .center)
                    }
                    .accessibility(label: Text(LocalizedStringKey("semanticLinkedIn \(title)")))
                }
                if (githubUrl != nil) {
                    Button {
                        linkOnClick(githubUrl!)
                    } label: {
                        Image("ic_github")
                            .renderingMode(.template)
                            .resizable()
                            .scaledToFit()
                            .frame(width: 24, height: 24, alignment: .center)
                    }
                    .accessibility(label: Text(LocalizedStringKey("semanticGitHub \(title)")))
                }
                if (websiteUrl != nil) {
                    Button {
                        linkOnClick(websiteUrl!)
                    } label: {
                        Image("ic_website")
                            .renderingMode(.template)
                            .resizable()
                            .scaledToFit()
                            .frame(width: 24, height: 24, alignment: .center)
                    }
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
            description: PartnerItemUi.companion.fake.description_,
            logoUrl: PartnerItemUi.companion.fake.logoUrl,
            twitterUrl: PartnerItemUi.companion.fake.twitterUrl,
            linkedInUrl: PartnerItemUi.companion.fake.linkedinUrl,
            githubUrl: SpeakerUi.companion.fake.githubUrl,
            websiteUrl: PartnerItemUi.companion.fake.siteUrl,
            linkOnClick: { url in }
        )
    }
}
