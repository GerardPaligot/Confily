//
//  TeamMemberViewModel.swift
//  iosApp
//
//  Created by Gérard PALIGOT on 02/01/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import Foundation
import SharedDi
import KMPNativeCoroutinesAsync

enum TeamMemberUiState {
    case loading
    case success(TeamMemberUi)
    case failure
}

@MainActor
class TeamMemberViewModel: ObservableObject {
    private let interactor: EventInteractor = InteractorHelper().eventInteractor
    
    @Published var uiState: TeamMemberUiState = TeamMemberUiState.loading
    private var teamTask: Task<(), Never>?
    let memberId: String

    init(memberId: String) {
        self.memberId = memberId
    }
    
    func fetchTeamMember() {
        teamTask = Task {
            do {
                let stream = asyncSequence(for: interactor.teamMember(memberId: memberId))
                for try await member in stream {
                    if (member != nil) {
                        self.uiState = .success(member!)
                    }
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
