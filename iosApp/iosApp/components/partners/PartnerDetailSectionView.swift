//
//  PartnerDetailSectionView.swift
//  iosApp
//
//  Created by GERARD on 30/12/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct PartnerDetailSectionView: View {
    var partnerUi: PartnerItemUi
    var color: Color = Color.c4hOnBackground
    var font: Font = Font.subheadline
    var twitterOnClick: (_: String) -> ()
    var linkedinOnClick: (_: String) -> ()
    var websiteOnClick: (_: String) -> ()
    
    var body: some View {
        HStack(alignment: .top, spacing: 8) {
            RemoteImage(
                url: partnerUi.logoUrl,
                description: nil,
                id: partnerUi.id
            )
            .padding()
            .frame(width: 128, height: 128)
            .background(Color.white)
            .clipShape(RoundedRectangle(cornerRadius: 8))
            VStack(alignment: .leading, spacing: 8) {
                Text(partnerUi.name)
                    .foregroundColor(color)
                    .font(font)
                    .fontWeight(.bold)
                Text(partnerUi.description_)
                    .foregroundColor(color)
                    .font(.caption)
            }
            Spacer()
            VStack {
                if (partnerUi.twitterUrl != nil) {
                    Button {
                        twitterOnClick(partnerUi.twitterUrl!)
                    } label: {
                        Image("ic_twitter")
                            .renderingMode(.template)
                            .resizable()
                            .scaledToFit()
                            .frame(width: 24, height: 24, alignment: .center)
                    }
                }
                if (partnerUi.linkedinUrl != nil) {
                    Button {
                        linkedinOnClick(partnerUi.linkedinUrl!)
                    } label: {
                        Image("ic_linkedin")
                            .renderingMode(.template)
                            .resizable()
                            .scaledToFit()
                            .frame(width: 24, height: 24, alignment: .center)
                    }
                }
                if (partnerUi.siteUrl != nil) {
                    Button {
                        websiteOnClick(partnerUi.siteUrl!)
                    } label: {
                        Image("ic_website")
                            .renderingMode(.template)
                            .resizable()
                            .scaledToFit()
                            .frame(width: 24, height: 24, alignment: .center)
                    }
                }
            }
        }
    }
}

struct PartnerDetailSectionView_Previews: PreviewProvider {
    static var previews: some View {
        PartnerDetailSectionView(
            partnerUi: PartnerItemUi.companion.fake,
            twitterOnClick: { url in },
            linkedinOnClick: { url in },
            websiteOnClick: { url in }
        )
    }
}
