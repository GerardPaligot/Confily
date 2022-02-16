//
//  ScheduleDetailVM.swift
//  iosApp
//
//  Created by GERARD on 15/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct ScheduleDetailVM: View {
    @ObservedObject var viewModel: ScheduleItemViewModel
    let scheduleId: String
    let onSpeakerClicked: (_: String) -> ()
    
    init(agendaRepository: AgendaRepository, scheduleId: String, speakerAction: @escaping (_: String) -> ()) {
        self.viewModel = ScheduleItemViewModel(repository: agendaRepository)
        self.scheduleId = scheduleId
        self.onSpeakerClicked = speakerAction
    }

    var body: some View {
        let uiState = viewModel.uiState
        Group {
            switch uiState {
                case .success(let talkUi):
                    ScheduleDetail(
                        talkUi: talkUi,
                        onSpeakerClicked: onSpeakerClicked
                    )
                case .failure(_):
                    Text("Something wrong happened")
                case .loading:
                    Text("Loading")
            }
        }
        .onAppear {
            viewModel.fetchScheduleDetails(scheduleId: scheduleId)
        }
    }
}
