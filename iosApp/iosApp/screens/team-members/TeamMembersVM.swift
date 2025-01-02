//
//  TeamMembersVM.swift
//  iosApp
//
//  Created by Gérard PALIGOT on 02/01/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI

struct TeamMembersVM: View {
    @ObservedObject var viewModel: TeamMembersViewModel

    var body: some View {
        let uiState = viewModel.uiState
        Group {
            switch uiState {
                case .success(let membersUi):
                TeamMembersUIView(members: membersUi)
                case .failure:
                    Text("textError")
                case .loading:
                    Text("textLoading")
            }
        }
        .onAppear {
            viewModel.fetchTeamMembers()
        }
        .onDisappear {
            viewModel.stop()
        }
    }
}
