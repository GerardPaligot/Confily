//
//  TeamMemberUIView.swift
//  iosApp
//
//  Created by Gérard PALIGOT on 02/01/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI
import SharedDi

struct TeamMemberUIView: View {
    let teamMember: TeamMemberUi
    
    var body: some View {
        List {
            Section {
                HStack {
                    Spacer()
                    SocialHeaderView(
                        title: teamMember.displayName,
                        pronouns: nil,
                        logoUrl: teamMember.photoUrl,
                        xUrl: teamMember.socials.first(where: { $0.type == SocialTypeUi.x })?.url,
                        mastodonUrl: teamMember.socials.first(where: { $0.type == SocialTypeUi.mastodon })?.url,
                        blueskyUrl: teamMember.socials.first(where: { $0.type == SocialTypeUi.bluesky })?.url,
                        facebookUrl: teamMember.socials.first(where: { $0.type == SocialTypeUi.facebook })?.url,
                        instagramUrl: teamMember.socials.first(where: { $0.type == SocialTypeUi.instagram })?.url,
                        youtubeUrl: teamMember.socials.first(where: { $0.type == SocialTypeUi.youtube })?.url,
                        linkedInUrl: teamMember.socials.first(where: { $0.type == SocialTypeUi.linkedin })?.url,
                        websiteUrl: teamMember.socials.first(where: { $0.type == SocialTypeUi.website })?.url,
                        emailUrl: teamMember.socials.first(where: { $0.type == SocialTypeUi.email })?.url
                    )
                    Spacer()
                }
            }
            Section {
                Text(teamMember.bio)
                    .font(Font.callout)
            }
        }
        .listStyle(.grouped)
    }
}

#Preview {
    TeamMemberUIView(
        teamMember: TeamMemberUi.companion.fake
    )
}
