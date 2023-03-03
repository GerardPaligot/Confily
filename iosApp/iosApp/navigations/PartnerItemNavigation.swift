//
//  PartnerItemNavigation.swift
//  iosApp
//
//  Created by GERARD on 03/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct PartnerItemNavigation: View {
    @EnvironmentObject var viewModelFactory: ViewModelFactory
    let partner: PartnerItemUi
    let size: CGFloat

    var body: some View {
        NavigationLink {
            PartnerDetailVM(
                viewModel: viewModelFactory.makePartnerDetailViewModel(partnerId: partner.id)
            )
        } label: {
            RemoteImage(
                url: partner.logoUrl,
                description: partner.name,
                id: partner.id
            )
            .padding()
            .frame(width: size, height: size)
            .background(Color.white)
            .clipShape(RoundedRectangle(cornerRadius: 8))
        }
    }
}
