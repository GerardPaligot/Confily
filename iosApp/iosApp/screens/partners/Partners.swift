//
//  Partners.swift
//  iosApp
//
//  Created by GERARD on 22/05/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct Partners: View {
    @Environment(\.openURL) var openURL
    var partners: PartnerGroupsUi

    var body: some View {
        GeometryReader { geometry in
            let parentWidth = geometry.size.width
            ScrollView {
                LazyVStack(spacing: 8) {
                    ForEach(Array(partners.map.keys).reversed(), id: \.self) { key in
                        Section {
                            PartnerDividerView(text: key)
                            ForEach(partners.map[key]!, id: \.[0].name) { partners in
                                PartnerRowView(partners: partners, parentWidth: parentWidth) { url in
                                    if let url2 = URL(string: url) { openURL(url2) }
                                }
                            }
                        }
                    }
                }
                .padding(.horizontal, 4)
            }
        }
    }
}

struct Partners_Previews: PreviewProvider {
    static var previews: some View {
        Partners(
            partners: PartnerGroupsUi.companion.fake
        )
    }
}
