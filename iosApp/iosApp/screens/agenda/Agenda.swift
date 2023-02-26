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
    let dates: [String]
    let agendas: [AgendaUi]
    let onFilteringClicked: () -> ()
    let talkItem: (TalkItemUi) -> TalkItem
    @State private var selectedDate = 0
    
    init(
        agendas: [String: AgendaUi],
        onFilteringClicked: @escaping () -> (),
        @ViewBuilder talkItem: @escaping (TalkItemUi) -> TalkItem
    ) {
        self.dates = agendas.keys.map({ key in key })
        self.agendas = agendas.values.map({ value in value })
        self.onFilteringClicked = onFilteringClicked
        self.talkItem = talkItem
    }

    var body: some View {
        let agenda = self.agendas[selectedDate]
        Group {
            ScrollView {
                LazyVStack {
                    if (self.dates.count > 1) {
                        Picker("Days:", selection: $selectedDate) {
                            ForEach(self.dates.indices, id: \.self) { index in
                                Text(self.dates[index]).tag(index)
                            }
                        }
                        .pickerStyle(SegmentedPickerStyle())
                    }
                    if (agenda.onlyFavorites && agenda.talks.isEmpty) {
                        NoFavoriteTalksView()
                    } else {
                        ForEach(agenda.talks.keys.sorted(), id: \.self) { time in
                            ScheduleItemView(
                                time: time,
                                talks: agenda.talks[time]!,
                                talkItem: self.talkItem
                            )
                        }
                    }
                }
                .padding([.horizontal], 16)
                .padding([.vertical], 24)
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
            agendas: [
                "01-01-2022": AgendaUi.companion.fake,
                "02-01-2022": AgendaUi.companion.fake
            ],
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
