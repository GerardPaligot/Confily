//
//  SpeakerItemNavigation.swift
//  iosApp
//
//  Created by GERARD on 17/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import SharedDi

struct SpeakerItemNavigation: View {
    @EnvironmentObject var viewModelFactory: ViewModelFactory
    var speaker: SpeakerItemUi
    
    var body: some View {
        NavigationLink {
            SpeakerDetailVM(
                viewModel: viewModelFactory.makeSpeakerViewModel(speakerId: speaker.id)
            )
        } label: {
            SpeakerItemView(url: speaker.photoUrl, title: speaker.displayName, description: speaker.activity)
        }
        .buttonStyle(.plain)
    }
}
