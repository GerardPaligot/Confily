//
//  TeamMembersViewModel.swift
//  iosApp
//
//  Created by Gérard PALIGOT on 02/01/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import Foundation
import SharedDi
import KMPNativeCoroutinesAsync

enum TeamMemberItemUiState {
    case loading
    case success(Dictionary<String, Array<TeamMemberItemUi>>)
    case failure
}

@MainActor
class TeamMembersViewModel: ObservableObject {
    private let interactor: EventInteractor = InteractorHelper().eventInteractor
    
    @Published var uiState: TeamMemberItemUiState = TeamMemberItemUiState.loading
    private var teamTask: Task<(), Never>?
    
    func fetchTeamMembers() {
        teamTask = Task {
            do {
                let stream = asyncSequence(for: interactor.teamMembers())
                for try await members in stream {
                    self.uiState = .success(members)
                }
            } catch {
                self.uiState = .failure
            }
        }
    }

    func stop() {
        teamTask?.cancel()
    }
}
