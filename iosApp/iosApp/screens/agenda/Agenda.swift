//
//  Agenda.swift
//  iosApp
//
//  Created by GERARD on 06/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct Agenda: View {
    @State private var selectedDate = ""
    let dates: [String]
    let agendas: [String : AgendaUi]
    let onFilteringClicked: () -> ()
    let onFavoriteClicked: (TalkItemUi) -> ()
    
    init(
        agendas: [String: AgendaUi],
        onFilteringClicked: @escaping () -> (),
        onFavoriteClicked: @escaping (TalkItemUi) -> ()
    ) {
        self.dates = agendas.keys.map({ key in key })
        self.selectedDate = self.dates.first ?? ""
        self.agendas = agendas
        self.onFilteringClicked = onFilteringClicked
        self.onFavoriteClicked = onFavoriteClicked
    }

    var body: some View {
        let selectedAgenda = self.agendas[self.selectedDate]
        NavigationView {
            VStack {
                if (self.agendas.keys.count > 1) {
                    Picker("Days:", selection: $selectedDate) {
                        ForEach(self.dates.indices, id: \.self) { index in
                            Text(self.dates[index]).tag(index)
                        }
                    }
                    .pickerStyle(SegmentedPickerStyle())
                }
                List {
                    if (selectedAgenda != nil && selectedAgenda!.onlyFavorites && selectedAgenda!.talks.isEmpty) {
                        NoFavoriteTalksView()
                    } else if (selectedAgenda != nil) {
                        ForEach(selectedAgenda!.talks.keys.sorted(), id: \.self) { time in
                            Section {
                                ForEach(selectedAgenda!.talks[time]!, id: \.id) { talk in
                                    if (!talk.isPause) {
                                        TalkItemNavigation(
                                            talk: talk,
                                            onFavoriteClicked: onFavoriteClicked
                                        )
                                        .swipeActions(allowsFullSwipe: false) {
                                            let iconName = talk.isFavorite ? "star.fill" : "star"
                                            let iconColor = talk.isFavorite ? Color.c4hSecondary : Color.c4hOnBackground
                                            let iconAction = talk.isFavorite ? LocalizedStringKey("actionRemoveFavorite") : LocalizedStringKey("actionToggleFavorite")
                                            Button { onFavoriteClicked(talk) } label: {
                                                Image(systemName: iconName)
                                            }
                                            .tint(iconColor)
                                            .accessibilityLabel(Text(iconAction))
                                        }
                                    } else {
                                        PauseView(title: talk.title)
                                    }
                                }
                            } header: {
                                Text(time)
                                    .accessibilityLabel(toSpelloutAccessibleTime(time: time))
                            }
                        }
                    }
                }
                .listStyle(.insetGrouped)
            }
            .navigationTitle(Text("screenAgenda"))
            .navigationBarTitleDisplayMode(.inline)
            .navigationBarItems(trailing:
                HStack {
                if (selectedAgenda != nil) {
                    Button(action: {
                        onFilteringClicked()
                    }, label: {
                        let icon = selectedAgenda!.onlyFavorites ? "line.3.horizontal.decrease.circle.fill" : "line.3.horizontal.decrease.circle"
                        Image(systemName: icon)
                            .accessibilityLabel("actionFilteringFavorites")
                            .accessibilityAddTraits(selectedAgenda!.onlyFavorites ? .isSelected : [])
                    })
                }
                }
            )
        }
    }
}

func toSpelloutAccessibleTime(time: String) -> LocalizedStringKey {
    let timeSplitted = time.split(separator: ":")
    let hh = Int(timeSplitted[0]) ?? 0
    let mm = Int(timeSplitted[1]) ?? 0
    return LocalizedStringKey("semanticHHmm \(hh) \(mm)")
}

struct Agenda_Previews: PreviewProvider {
    static var previews: some View {
        Agenda(
            agendas: [
                "01-01-2022": AgendaUi.companion.fake,
                "02-01-2022": AgendaUi.companion.fake
            ],
            onFilteringClicked: {},
            onFavoriteClicked: { talk in }
        )
        .environmentObject(ViewModelFactory())
    }
}
