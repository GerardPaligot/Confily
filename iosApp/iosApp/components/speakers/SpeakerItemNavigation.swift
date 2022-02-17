//
//  SpeakerItemNavigation.swift
//  iosApp
//
//  Created by GERARD on 17/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct SpeakerItemNavigation: View {
    var agendaRepository: AgendaRepository
    var speaker: SpeakerItemUi
    
    var body: some View {
        NavigationLink {
            SpeakerDetailVM(
                agendaRepository: agendaRepository,
                speakerId: speaker.id,
                twitterAction: { String in },
                githubAction: { String in }
            )
        } label: {
            SpeakerItemView(speakerUi: speaker)
        }
    }
}
