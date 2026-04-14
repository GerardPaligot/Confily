//
//  TeamMemberItemNavigation.swift
//  iosApp
//
//  Created by Gérard PALIGOT on 02/01/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI
import SharedDi

struct TeamMemberItemNavigation: View {
    @EnvironmentObject var viewModelFactory: ViewModelFactory
    @State private var showDetail = false
    var teamMember: TeamMemberItemUi
    
    var body: some View {
        Button {
            showDetail = true
        } label: {
            SpeakerItemView(url: teamMember.url, title: teamMember.displayName, description: teamMember.role)
        }
        .buttonStyle(.plain)
        .sheet(isPresented: $showDetail) {
            NavigationView {
                TeamMemberVM(
                    viewModel: viewModelFactory.makeTeamMemberViewModel(memberId: teamMember.id)
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
