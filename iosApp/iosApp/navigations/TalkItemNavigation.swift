//
//  TalkItemNavigation.swift
//  iosApp
//
//  Created by GERARD on 03/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct TalkItemNavigation: View {
    @EnvironmentObject var viewModelFactory: ViewModelFactory
    let talk: TalkItemUi
    let onFavoriteClicked: (TalkItemUi) -> ()
    
    init(talk: TalkItemUi, onFavoriteClicked: @escaping (TalkItemUi) -> Void) {
        self.talk = talk
        self.onFavoriteClicked = onFavoriteClicked
    }

    var body: some View {
        NavigationLink {
            ScheduleDetailVM(
                viewModel: viewModelFactory.makeScheduleItemViewModel(scheduleId: talk.id)
            )
        } label: {
            TalkItemView(
                talk: talk,
                onFavoriteClicked: onFavoriteClicked
            )
        }
        .buttonStyle(.plain)
    }
}
