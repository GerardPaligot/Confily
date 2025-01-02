//
//  TeamMemberVM.swift
//  iosApp
//
//  Created by Gérard PALIGOT on 02/01/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI

struct TeamMemberVM: View {
    @ObservedObject var viewModel: TeamMemberViewModel

    var body: some View {
        let uiState = viewModel.uiState
        Group {
            switch uiState {
                case .success(let memberUi):
                    TeamMemberUIView(teamMember: memberUi)
                case .failure:
                    Text("textError")
                case .loading:
                    Text("textLoading")
            }
        }
        .onAppear {
            viewModel.fetchTeamMember()
        }
        .onDisappear {
            viewModel.stop()
        }
    }
}
