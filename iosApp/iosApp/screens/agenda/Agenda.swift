//
//  Agenda.swift
//  iosApp
//
//  Created by GERARD on 06/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct Agenda<TalkItem: View>: View {
    let agenda: AgendaUi
    let onFilteringClicked: () -> ()
    let talkItem: (TalkItemUi) -> TalkItem
    
    init(agenda: AgendaUi, onFilteringClicked: @escaping () -> (), @ViewBuilder talkItem: @escaping (TalkItemUi) -> TalkItem) {
        self.agenda = agenda
        self.onFilteringClicked = onFilteringClicked
        self.talkItem = talkItem
    }

    var body: some View {
        ScrollView {
            LazyVStack {
                ForEach(agenda.talks.keys.sorted(), id: \.self) { time in
                    ScheduleItemView(
                        time: time,
                        talks: agenda.talks[time]!,
                        talkItem: self.talkItem
                    )
                }
            }
        }
        .navigationTitle(Text("screenAgenda"))
        .navigationBarTitleDisplayMode(.inline)
        .navigationBarItems(trailing:
            HStack {
                Button(action: {
                    onFilteringClicked()
                }, label: {
                    let icon = agenda.onlyFavorites ? "line.3.horizontal.decrease.circle.fill" : "line.3.horizontal.decrease.circle"
                    Image(systemName: icon)
                        .accessibilityLabel("actionFilteringFavorites")
                        .accessibilityAddTraits(agenda.onlyFavorites ? .isSelected : [])
                })
            }
        )
    }
}

struct Agenda_Previews: PreviewProvider {
    static var previews: some View {
        Agenda(
            agenda: AgendaUi.companion.fake,
            onFilteringClicked: {},
            talkItem: { talk in
                TalkItemView(
                    talk: talk,
                    onFavoriteClicked: { TalkItemUi in }
                )
            }
        )
    }
}
