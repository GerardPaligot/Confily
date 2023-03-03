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

    var body: some View {
        let uiState = viewModel.uiState
        Group {
            switch uiState {
                case .success(let talkUi):
                    ScheduleDetail(talkUi: talkUi)
                case .loading:
                    Text("textLoading")
                    .onAppear {
                        viewModel.fetchScheduleDetails()
                    }
            }
        }
    }
}
