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
    @State private var showDetail = false
    var speaker: SessionSpeakerItemUi
    
    var body: some View {
        Button {
            showDetail = true
        } label: {
            SpeakerItemView(url: speaker.photoUrl, title: speaker.displayName, description: speaker.activity)
        }
        .sheet(isPresented: $showDetail) {
            NavigationView {
                SpeakerDetailVM(
                    viewModel: viewModelFactory.makeSpeakerViewModel(speakerId: speaker.id)
                )
                .toolbar {
                    ToolbarItem(placement: .confirmationAction) {
                        Button {
                            showDetail = false
                        } label: {
                            Image(systemName: "xmark.circle.fill")
                                .foregroundStyle(.gray)
                        }
                    }
                }
            }
            .modifier(PresentationDetentsModifier())
        }
    }
}

private struct PresentationDetentsModifier: ViewModifier {
    func body(content: Content) -> some View {
        if #available(iOS 16.0, *) {
            content.presentationDetents([.medium, .large])
        } else {
            content
        }
    }
}
