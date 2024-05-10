//
//  EventSessionItemNavigation.swift
//  iosApp
//
//  Created by GERARD on 10/05/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import SharedDi

struct EventSessionItemNavigation: View {
    @EnvironmentObject var viewModelFactory: ViewModelFactory
    let session: EventSessionItemUi

    var body: some View {
        if (session.description_ == nil) {
            EventSessionView(session: session)
        } else {
            NavigationLink {
                ScheduleEventSessionVM(
                    viewModel: viewModelFactory.makeScheduleEventSessionViewModel(scheduleId: session.id)
                )
            } label: {
                EventSessionView(session: session)
            }
            .buttonStyle(.plain)
        }
    }
}
