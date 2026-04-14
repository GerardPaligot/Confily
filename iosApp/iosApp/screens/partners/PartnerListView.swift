//
//  PartnerListView.swift
//  iosApp
//
//  Created by Gérard PALIGOT on 04/12/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import SharedDi

private extension Array {
    func chunked(_ size: Int) -> [[Element]] {
        stride(from: 0, to: count, by: size).map {
            Array(self[$0..<Swift.min($0 + size, count)])
        }
    }
}

struct PartnerListView: View {
    let partners: PartnerGroupsUi
    let horizontalSpacing: CGFloat = 16
    let maxItems: Int = 3

    var body: some View {
        ScrollView {
            VStack(spacing: 8) {
                ForEach(partners.groups, id: \.type) { partnerGroup in
                    Section {
                        VStack(spacing: 8) {
                            let rows = partnerGroup.partners.chunked(maxItems)
                            ForEach(0..<rows.count, id: \.self) { rowIndex in
                                let row = rows[rowIndex]
                                HStack(spacing: 8) {
                                    ForEach(row, id: \.id) { partner in
                                        PartnerItemNavigation(partner: partner)
                                            .frame(maxWidth: .infinity)
                                    }
                                    if row.count < maxItems {
                                        ForEach(0..<(maxItems - row.count), id: \.self) { _ in
                                            Color.clear.frame(maxWidth: .infinity)
                                        }
                                    }
                                }
                            }
                        }
                        .padding(.horizontal, self.horizontalSpacing)
                    } header : {
                        PartnerDividerView(text: partnerGroup.type)
                            .padding(.horizontal, self.horizontalSpacing)
                    }
                }
            }
        }
    }
}

#Preview {
    PartnerListView(partners: PartnerGroupsUi.companion.fake)
        .environmentObject(ViewModelFactory())
}
