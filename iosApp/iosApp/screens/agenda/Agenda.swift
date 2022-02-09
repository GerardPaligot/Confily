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
    var agenda: AgendaUi
    var onTalkClicked: (_: String) -> ()
    var onFavoriteClicked: (_: String, _: Bool) -> ()

    var body: some View {
        ScrollView {
            LazyVStack {
                ForEach(agenda.talks.keys.sorted(), id: \.self) { time in
                    ScheduleItemView(
                        time: time,
                        talks: agenda.talks[time]!,
                        onTalkClicked: onTalkClicked,
                        onFavoriteClicked: onFavoriteClicked
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
            onTalkClicked: { String in },
            onFavoriteClicked: { String, Bool in }
        )
    }
}
