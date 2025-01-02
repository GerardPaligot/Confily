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
    var teamMember: TeamMemberItemUi
    
    var body: some View {
        NavigationLink {
            TeamMemberVM(
                viewModel: viewModelFactory.makeTeamMemberViewModel(memberId: teamMember.id)
            )
        } label: {
            SpeakerItemView(url: teamMember.url, title: teamMember.displayName, description: teamMember.role)
        }
        .buttonStyle(.plain)
    }
}
