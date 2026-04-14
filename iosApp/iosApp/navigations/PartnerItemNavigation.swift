//
//  PartnerItemNavigation.swift
//  iosApp
//
//  Created by GERARD on 03/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import SharedDi

struct PartnerItemNavigation: View {
    @EnvironmentObject var viewModelFactory: ViewModelFactory
    let partner: PartnerItemUi

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
            .frame(maxWidth: .infinity)
            .aspectRatio(1, contentMode: .fill)
            .background(Color.white)
            .clipShape(RoundedRectangle(cornerRadius: 8))
        }
    }
}
