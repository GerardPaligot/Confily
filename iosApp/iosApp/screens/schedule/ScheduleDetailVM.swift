//
//  ScheduleDetailVM.swift
//  iosApp
//
//  Created by GERARD on 15/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct ScheduleDetailVM<SpeakerItem: View>: View {
    @ObservedObject var viewModel: ScheduleItemViewModel
    let scheduleId: String
    let speakerItem: (SpeakerItemUi) -> SpeakerItem
    
    init(agendaRepository: AgendaRepository, scheduleId: String, @ViewBuilder speakerItem: @escaping (SpeakerItemUi) -> SpeakerItem) {
        self.viewModel = ScheduleItemViewModel(repository: agendaRepository)
        self.scheduleId = scheduleId
        self.speakerItem = speakerItem
    }

    var body: some View {
        let uiState = viewModel.uiState
        Group {
            switch uiState {
                case .success(let talkUi):
                    ScheduleDetail(
                        talkUi: talkUi,
                        speakerItem: speakerItem
                    )
                case .failure(_):
                    Text("textError")
                case .loading:
                    Text("textLoading")
            }
        }
        .onAppear {
            viewModel.fetchScheduleDetails(scheduleId: scheduleId)
        }
    }
}
