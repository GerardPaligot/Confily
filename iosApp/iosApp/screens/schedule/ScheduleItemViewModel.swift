//
//  ScheduleItemViewModel.swift
//  iosApp
//
//  Created by GERARD on 15/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import shared

enum ScheduleUiState {
    case loading
    case success(TalkUi)
    case failure(Error)
}

class ScheduleItemViewModel: ObservableObject {
    let repository: AgendaRepository

    init(repository: AgendaRepository) {
        print("Init")
        self.repository = repository
    }

    @Published var uiState: ScheduleUiState = ScheduleUiState.loading
    
    func fetchScheduleDetails(scheduleId: String) {
        print("Fetch")
        repository.scheduleItem(scheduleId: scheduleId) { talkUi, error in
            if (talkUi != nil) {
                print("Success")
                self.uiState = ScheduleUiState.success(talkUi!)
            } else {
                print("Error")
                self.uiState = ScheduleUiState.failure(error!)
            }
        }
    }
}
