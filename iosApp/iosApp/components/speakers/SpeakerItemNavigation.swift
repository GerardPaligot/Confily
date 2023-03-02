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
    var viewModel: SpeakerViewModel
    var speaker: SpeakerItemUi
    
    var body: some View {
        NavigationLink {
            SpeakerDetailVM(
                viewModel: viewModel,
                speakerId: speaker.id
            )
        } label: {
            SpeakerItemView(speakerUi: speaker)
        }
    }
}
