//
//  Agenda.swift
//  iosApp
//
//  Created by GERARD on 06/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import SharedDi

struct Agenda: View {
    @State private var selectedDate = ""
    @State private var scrollTarget: String?
    @State private var showNowUnavailable = false
    let dates: [String]
    let agendas: [String : AgendaUi]
    let isCurrentDay: Bool
    let currentTimeSlotKey: String?
    let onFavoriteClicked: (TalkItemUi) -> ()
    
    init(
        agendas: [String: AgendaUi],
        isCurrentDay: Bool,
        currentTimeSlotKey: String?,
        onFavoriteClicked: @escaping (TalkItemUi) -> ()
    ) {
        self.dates = agendas.keys.map({ key in key }).sorted()
        self.selectedDate = self.dates.first ?? ""
        self.agendas = agendas
        self.isCurrentDay = isCurrentDay
        self.currentTimeSlotKey = currentTimeSlotKey
        self.onFavoriteClicked = onFavoriteClicked
    }

    private var todayKey: String {
        let formatter = DateFormatter()
        formatter.dateFormat = "dd/MM/yyyy"
        return formatter.string(from: Date())
    }

    var body: some View {
        let selectedAgenda = self.agendas[self.selectedDate]
        NavigationView {
            VStack {
                if (self.agendas.keys.count > 1) {
                    Picker("Days:", selection: $selectedDate) {
                        ForEach(self.dates.indices, id: \.self) { index in
                            Text(self.dates[index]).tag(self.dates[index])
                        }
                    }
                    .pickerStyle(SegmentedPickerStyle())
                }
                ScrollViewReader { proxy in
                    List {
                        if (selectedAgenda != nil && selectedAgenda!.onlyFavorites && selectedAgenda!.sessions.isEmpty) {
                            NoFavoriteTalksView()
                        } else if (selectedAgenda != nil) {
                            ForEach(selectedAgenda!.sessions.keys.sorted(), id: \.self) { time in
                                Section {
                                    let sessionItems = selectedAgenda!.sessions[time]!
                                    ForEach(0..<sessionItems.count, id: \.self) { talkIndex in
                                        let session = sessionItems[talkIndex]
                                        if let talk = session as? TalkItemUi {
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
                                        }
                                        if let eventSession = session as? EventSessionItemUi {
                                            EventSessionItemNavigation(session: eventSession)
                                        }
                                    }
                                } header: {
                                    Text(time)
                                        .accessibilityLabel(toSpelloutAccessibleTime(time: time))
                                }
                                .id(time)
                            }
                        }
                    }
                    .listStyle(.insetGrouped)
                    .onChange(of: scrollTarget) { target in
                        if let target = target {
                            withAnimation {
                                proxy.scrollTo(target, anchor: .top)
                            }
                            scrollTarget = nil
                        }
                    }
                }
            }
            .navigationTitle(Text("screenAgenda"))
            .navigationBarTitleDisplayMode(.inline)
            .navigationBarItems(trailing:
                HStack {
                    Button {
                        if isCurrentDay {
                            let today = todayKey
                            if dates.contains(today) {
                                selectedDate = today
                            }
                            if let timeSlot = currentTimeSlotKey {
                                scrollTarget = timeSlot
                            }
                        } else {
                            showNowUnavailable = true
                        }
                    } label: {
                        Image(systemName: "clock")
                    }
                    AgendaFiltersNavigation()
                }
            )
            .alert(Text("textNowNotToday"), isPresented: $showNowUnavailable) {
                Button("OK", role: .cancel) {}
            }
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
            isCurrentDay: true,
            currentTimeSlotKey: "10:00",
            onFavoriteClicked: { talk in }
        )
        .environmentObject(ViewModelFactory())
    }
}
