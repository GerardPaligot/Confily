//
//  ScheduleEventSessionVM.swift
//  iosApp
//
//  Created by GERARD on 10/05/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import SharedDi

struct ScheduleEventSessionVM: View {
    @ObservedObject var viewModel: ScheduleEventSessionViewModel

    var body: some View {
        let uiState = viewModel.uiState
        Group {
            switch uiState {
                case .success(let sessionUi):
                    ScheduleEventSessionDetail(session: sessionUi)
                case .failure:
                    Text("textError")
                case .loading:
                    Text("textLoading")
            }
        }
        .onAppear {
            viewModel.fetchScheduleDetails()
        }
        .onDisappear {
            viewModel.stop()
        }
    }
}
