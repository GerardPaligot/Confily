//
//  TeamMembersNavigation.swift
//  iosApp
//
//  Created by Gérard PALIGOT on 02/01/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI

struct TeamMembersNavigation: View {
    @EnvironmentObject var viewModelFactory: ViewModelFactory

    var body: some View {
        NavigationLink {
            TeamMembersVM(viewModel: self.viewModelFactory.makeTeamMembersViewModel())
        } label: {
            Text("actionsTeam")
        }
    }
}
