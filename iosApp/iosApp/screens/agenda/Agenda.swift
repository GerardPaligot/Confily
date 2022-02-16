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
    let talkItem: (TalkItemUi) -> TalkItem
    
    init(agenda: AgendaUi, @ViewBuilder talkItem: @escaping (TalkItemUi) -> TalkItem) {
        self.agenda = agenda
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
    }
}

struct Agenda_Previews: PreviewProvider {
    static var previews: some View {
        Agenda(
            agenda: AgendaUi.companion.fake,
            talkItem: { talk in
                TalkItemView(
                    talk: talk,
                    onFavoriteClicked: { String, Bool in }
                )
            }
        )
    }
}
