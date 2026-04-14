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
    @State private var showDetail = false
    let partner: PartnerItemUi

    var body: some View {
        Button {
            showDetail = true
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
        .sheet(isPresented: $showDetail) {
            NavigationView {
                PartnerDetailVM(
                    viewModel: viewModelFactory.makePartnerDetailViewModel(partnerId: partner.id)
                )
                .toolbar {
                    ToolbarItem(placement: .confirmationAction) {
                        Button {
                            showDetail = false
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

private struct PresentationDetentsModifier: ViewModifier {
    func body(content: Content) -> some View {
        if #available(iOS 16.0, *) {
            content.presentationDetents([.medium, .large])
        } else {
            content
        }
    }
}
